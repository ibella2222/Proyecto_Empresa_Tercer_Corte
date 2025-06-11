package com.example.company.controller;

import com.example.company.dto.CompanyDTO;
import com.example.company.entity.Company;
import com.example.company.repository.CompanyRepository;
import com.example.company.service.CompanyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository; // Inyección directa ya estaba
    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    // Constructor jejeej unificado para las dependencias
    public CompanyController(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    @PostMapping("/register/{username}")
    public ResponseEntity<Map<String,Object>> registerCompany(
            @PathVariable String username,
            @Valid @RequestBody CompanyDTO companyDTO) {

        companyService.registerCompany(
                username,
                companyDTO.getNit(),
                companyDTO.getName(),
                companyDTO.getSector(),
                companyDTO.getContactPhone(),
                companyDTO.getContactFirstName(),
                companyDTO.getContactLastName(),
                companyDTO.getContactPosition()
        );

        return ResponseEntity.ok(Map.of("message", "Empresa registrada correctamente"));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getCompanyByUsername(@PathVariable String username) {
        return companyService.findByUsername(username)
                .map(this::convertToDto) // Convertir a DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check-nit/{nit}")
    public ResponseEntity<?> checkNitExists(@PathVariable String nit) {
        boolean exists = companyRepository.existsByNit(nit);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * CORRECCIÓN: Este método ahora devuelve un CompanyDTO.
     * Es el endpoint que tu cliente necesita para obtener los detalles de la empresa.
     */
    @GetMapping("/nit/{nit}")
    public ResponseEntity<CompanyDTO> findByNit(@PathVariable String nit) {
        Optional<Company> companyOptional = companyService.findByNit(nit);

        // Mapea el Optional<Company> a un Optional<CompanyDTO>
        return companyOptional.map(this::convertToDto)
                .map(ResponseEntity::ok) // Si el DTO existe, devuelve 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no, devuelve 404
    }

    /**
     * Método auxiliar para convertir la entidad Company a un CompanyDTO.
     * Esto evita exponer la entidad interna en la API.
     */
    private CompanyDTO convertToDto(Company company) {
        return new CompanyDTO(
                company.getUsername(),
                company.getNit(),
                company.getName(),
                company.getSector(),
                company.getContactPhone(),
                company.getContactFirstName(),
                company.getContactLastName(),
                company.getContactPosition()
        );
    }
}