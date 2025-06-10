package com.example.company.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.company.dto.CompanyDTO;
import com.example.company.entity.Company;

import com.example.company.repository.CompanyRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Register a new company based on an existing user
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Company registerCompany(String username, String nit, String name, String sector,
                                   String contactPhone, String contactFirstName,
                                   String contactLastName, String contactPosition) {
        try {
            logger.info("Iniciando registro de empresa para user: {}", username);

            // Verificar que no existe una empresa con el mismo NIT
            if (companyRepository.existsByNit(nit)) {
                throw new RuntimeException("Ya existe una empresa con el NIT: " + nit);
            }

            // Crear y poblar nueva Company usando JPA en lugar de SQL nativo
            logger.info("Creando registro de empresa en la base de datos");
            Company company = new Company();
            company.setUsername(username);
            company.setNit(nit);
            company.setName(name);
            company.setSector(sector);
            company.setContactPhone(contactPhone);
            company.setContactFirstName(contactFirstName);
            company.setContactLastName(contactLastName);
            company.setContactPosition(contactPosition);

            try {
                company = companyRepository.save(company);
                logger.info("Empresa guardada exitosamente  {}");
            } catch (Exception e) {
                logger.error("Error al guardar empresa: {}", e.getMessage(), e);
                throw new RuntimeException("Error al insertar datos de empresa: " + e.getMessage(), e);
            }

            // Create DTO for messaging
            CompanyDTO companyDTO = new CompanyDTO(
                    company.getUsername(),
                    company.getNit(),
                    company.getName(),
                    company.getSector(),
                    company.getContactPhone(),
                    company.getContactFirstName(),
                    company.getContactLastName(),
                    company.getContactPosition()
            );


            logger.info("Empresa registrada exitosamente: {}", company.getName());
            return company;
        } catch (Exception e) {
            logger.error("Error en registerCompany: {}", e.getMessage(), e);
            throw new RuntimeException("Error al registrar empresa: " + e.getMessage(), e);
        }
    }

    public Optional<Company> findByUsername(String username) {
        logger.info("Buscando empresa para el usuario: {}", username);
        return companyRepository.findByUsername(username);
    }
    public Optional<Company> findByNit(String nit) {
        return companyRepository.findByNit(nit);
    }

}