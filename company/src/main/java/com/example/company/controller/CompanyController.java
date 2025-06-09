package com.example.company.controller;
import java.util.Optional;
import com.example.company.service.CompanyService;
import com.example.company.dto.CompanyDTO;
import com.example.company.entity.Company;
import com.example.company.config.RabbitMQConfig;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    public CompanyController(CompanyService companyService,
                             RabbitTemplate rabbitTemplate) {
        this.companyService = companyService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/register/{username}")
    public ResponseEntity<Map<String,Object>> registerCompany(
            @PathVariable String username,
            @Valid @RequestBody CompanyDTO companyDTO) {

        Company saved = companyService.registerCompany(
                username,
                companyDTO.getNit(),
                companyDTO.getName(),
                companyDTO.getSector(),
                companyDTO.getContactPhone(),
                companyDTO.getContactFirstName(),
                companyDTO.getContactLastName(),
                companyDTO.getContactPosition()
        );




        return ResponseEntity.ok(Map.of(
                "message", "Empresa registrada correctamente"
        ));
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getCompanyByUsername(@PathVariable String username) {
        try {
            Optional<Company> companyOpt = companyService.findByUsername(username);

            if (companyOpt.isPresent()) {
                Company company = companyOpt.get();
                logger.info("Empresa encontrada para usuario: {}", username);
                return ResponseEntity.ok(company);
            } else {
                logger.warn("No se encontró empresa para el usuario: {}", username);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al buscar empresa para usuario {}: {}", username, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
