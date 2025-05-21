package com.example.login.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.login.Config.RabbitMQCompanyConfig;
import com.example.login.DTO.CompanyDTO;
import com.example.login.Entities.Company;
import com.example.login.Entities.User;
import com.example.login.Repository.CompanyRepository;
import com.example.login.Service.CompanyService;
import com.example.login.Service.UserService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/register/{userId}")
    public ResponseEntity<?> registerCompany(@PathVariable int userId, @RequestBody CompanyDTO companyDTO) {
        logger.info("Solicitud de registro de empresa recibida para userId: {}", userId);
        try {
            Optional<User> optionalUser = userService.findById(userId);

            if (optionalUser.isEmpty()) {
                logger.warn("Usuario no encontrado con ID: {}", userId);
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }

            User user = optionalUser.get();
            if (!"Empresa".equalsIgnoreCase(user.getRole())) {
                logger.warn("El usuario {} no tiene el rol de EMPRESA, rol actual: {}", userId, user.getRole());
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario no tiene el rol de EMPRESA"));
            }

            if (companyRepository.existsById(userId)) {
                logger.warn("El usuario {} ya está registrado como empresa", userId);
                return ResponseEntity.status(409).body(Map.of("error", "El usuario ya está registrado como empresa"));
            }

            Company savedCompany = companyService.registerCompany(
                    userId,
                    companyDTO.getNit(),
                    companyDTO.getName(),
                    companyDTO.getSector(),
                    companyDTO.getContactPhone(),
                    companyDTO.getContactFirstName(),
                    companyDTO.getContactLastName(),
                    companyDTO.getContactPosition()
            );

            // Convertir entidad a DTO antes de enviarla por RabbitMQ
            CompanyDTO dto = new CompanyDTO();
            dto.setNit(savedCompany.getNit());
            dto.setName(savedCompany.getName());
            dto.setSector(savedCompany.getSector());
            dto.setContactPhone(savedCompany.getContactPhone());
            dto.setContactFirstName(savedCompany.getContactFirstName());
            dto.setContactLastName(savedCompany.getContactLastName());
            dto.setContactPosition(savedCompany.getContactPosition());

            logger.info("Enviando CompanyDTO a RabbitMQ con NIT: {}", dto.getNit());

            rabbitTemplate.convertAndSend(
                    RabbitMQCompanyConfig.COMPANY_EXCHANGE,
                    RabbitMQCompanyConfig.COMPANY_ROUTING_KEY,
                    dto
            );

            logger.info("Empresa registrada exitosamente con ID: {}", savedCompany.getId());
            return ResponseEntity.ok(Map.of(
                    "message", "Empresa registrada correctamente",
                    "companyId", savedCompany.getId()
            ));

        } catch (Exception e) {
            logger.error("Error al registrar empresa: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al registrar empresa: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/check-nit/{nit}")
    public ResponseEntity<?> checkNitExists(@PathVariable String nit) {
        logger.info("Verificando existencia de empresa con NIT: {}", nit);
        try {
            boolean exists = companyRepository.existsByNit(nit);
            logger.info("Resultado de verificación de NIT {}: {}", nit, exists);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            logger.error("Error al verificar NIT: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al verificar NIT: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies() {
        logger.info("Solicitando lista de todas las empresas");
        try {
            List<Company> companies = companyRepository.findAll();
            logger.info("Recuperadas {} empresas", companies.size());
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            logger.error("Error al recuperar empresas: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al recuperar empresas: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCompanyByUserId(@PathVariable int userId) {
        logger.info("Buscando empresa para el usuario con ID: {}", userId);
        try {
            Optional<Company> company = companyRepository.findByUserId(userId);

            if (company.isEmpty()) {
                logger.warn("No se encontró empresa para el usuario con ID: {}", userId);
                return ResponseEntity.status(404).body(Map.of(
                        "error", "No se encontró empresa para el usuario especificado"
                ));
            }

            logger.info("Empresa encontrada: {}", company.get().getName());
            return ResponseEntity.ok(company.get());
        } catch (Exception e) {
            logger.error("Error al buscar empresa por userId: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al buscar empresa: " + e.getMessage()
            ));
        }
    }
}
