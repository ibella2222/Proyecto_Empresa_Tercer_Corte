package com.example.login.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Inyectar RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Registra una empresa basándose en un usuario ya existente.
     * @param userId ID del usuario ya registrado.
     * @param companyDTO datos adicionales de la empresa.
     * @return ResponseEntity con resultado del registro.
     */
    @PostMapping("/register/{userId}")
    public ResponseEntity<?> registerCompany(@PathVariable int userId, @RequestBody CompanyDTO companyDTO) {
        logger.info("Solicitud de registro de empresa recibida para userId: {}", userId);
        try {
            logger.info("Buscando usuario con ID: {}", userId);
            Optional<User> optionalUser = userService.findById(userId);
    
            if (optionalUser.isEmpty()) {
                logger.warn("Usuario no encontrado con ID: {}", userId);
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
    
            User user = optionalUser.get();
            logger.info("Usuario encontrado: {}, rol: {}", user.getUsername(), user.getRole());
    
            // Validar que tenga rol de empresa
            if (!"Empresa".equalsIgnoreCase(user.getRole())) {
                logger.warn("El usuario {} no tiene el rol de EMPRESA, rol actual: {}", userId, user.getRole());
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario no tiene el rol de EMPRESA"));
            }
    
            // Verificar que aún no esté registrado como empresa
            if (companyRepository.existsById(userId)) {
                logger.warn("El usuario {} ya está registrado como empresa", userId);
                return ResponseEntity.status(409).body(Map.of("error", "El usuario ya está registrado como empresa"));
            }
    
            logger.info("Procediendo a registrar empresa para userId: {}", userId);
            // Registrar empresa usando el userId del path parameter
            Company savedCompany = companyService.registerCompany(
                userId,  // Usa el userId del path parameter
                companyDTO.getNit(),
                companyDTO.getName(),
                companyDTO.getSector(),
                companyDTO.getContactPhone(),
                companyDTO.getContactFirstName(),
                companyDTO.getContactLastName(),
                companyDTO.getContactPosition()
            );

            // Enviamos el objeto savedCompany en vez del DTO
            logger.info("Enviando notificación a la cola de mensajes para la empresa: {}", savedCompany.getName());
            rabbitTemplate.convertAndSend(
                RabbitMQCompanyConfig.COMPANY_EXCHANGE,
                RabbitMQCompanyConfig.COMPANY_ROUTING_KEY,
                savedCompany
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

    /**
     * Verifica si existe una empresa con el NIT proporcionado.
     * @param nit NIT de la empresa a verificar.
     * @return ResponseEntity indicando si existe o no.
     */
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

    /**
     * Obtiene todas las empresas registradas.
     * @return Lista de todas las empresas.
     */
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

    /**
     * Obtiene una empresa por su ID de usuario.
     * @param userId ID del usuario asociado a la empresa.
     * @return La empresa correspondiente al usuario.
     */
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