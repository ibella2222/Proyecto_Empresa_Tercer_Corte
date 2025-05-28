package com.example.login.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.login.DTO.CompanyDTO;
import com.example.login.Entities.Company;
import com.example.login.Entities.User;
import com.example.login.Messaging.CompanyRegistrationSender;
import com.example.login.Repository.CompanyRepository;
import com.example.login.Repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CompanyService {
    
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
    
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyRegistrationSender registrationSender;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, CompanyRegistrationSender registrationSender) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.registrationSender = registrationSender;
    }
    
    /**
     * Register a new company based on an existing user
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Company registerCompany(int userId, String nit, String name, String sector, 
                                  String contactPhone, String contactFirstName, 
                                  String contactLastName, String contactPosition) {
        try {
            logger.info("Iniciando registro de empresa para userId: {}", userId);
            
            // Check if a company with this ID already exists
            if (companyRepository.existsById(userId)) {
                logger.warn("El usuario {} ya es una empresa", userId);
                throw new RuntimeException("El usuario ya es una empresa");
            }
            
            // Get the user with pessimistic locking
            logger.info("Buscando usuario con ID: {}", userId);
            User user = userRepository.findByIdWithLock(userId)
                    .orElseThrow(() -> {
                        logger.error("Usuario no encontrado con ID: {}", userId);
                        return new RuntimeException("Usuario no encontrado con ID: " + userId);
                    });
            
            logger.info("Usuario encontrado: {}, con rol: {}", user.getUsername(), user.getRole());
            
            // Validar rol de usuario - Aceptar tanto "Empresa" como "EMPRESA"
            String userRole = user.getRole();
            if (!"Empresa".equalsIgnoreCase(userRole)) {
                logger.warn("El usuario {} no tiene el rol correcto. Rol actual: {}", userId, userRole);
                throw new RuntimeException("El usuario no tiene el rol de EMPRESA");
            }
            
            // Create and populate a new Company directly with SQL
            logger.info("Insertando registro de empresa en la base de datos");
            try {
                entityManager.createNativeQuery(
                    "INSERT INTO companies (user_id, nit, name, sector, contact_phone, contact_first_name, contact_last_name, contact_position) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                    .setParameter(1, userId)
                    .setParameter(2, nit)
                    .setParameter(3, name)
                    .setParameter(4, sector)
                    .setParameter(5, contactPhone)
                    .setParameter(6, contactFirstName)
                    .setParameter(7, contactLastName)
                    .setParameter(8, contactPosition)
                    .executeUpdate();
                
                // Ya no intentamos actualizar la columna user_type que no existe
                // La siguiente línea fue comentada para evitar el error:
                /*
                entityManager.createNativeQuery(
                    "UPDATE users SET user_type = 'COMPANY' WHERE id = ?")
                    .setParameter(1, userId)
                    .executeUpdate();
                */
                
                // Si se necesita marcar al usuario como empresa, podemos actualizar el campo 'role' que sí existe
                // Pero solo si es necesario y si no se está validando en otra parte
                // Comentado por ahora ya que parece que el rol ya está establecido correctamente
                /*
                entityManager.createNativeQuery(
                    "UPDATE users SET role = 'EMPRESA' WHERE id = ?")
                    .setParameter(1, userId)
                    .executeUpdate();
                */
            } catch (Exception e) {
                logger.error("Error SQL al insertar empresa: {}", e.getMessage(), e);
                throw new RuntimeException("Error al insertar datos de empresa: " + e.getMessage(), e);
            }
            
            // Clear persistence context to force a fresh load
            entityManager.flush();
            entityManager.clear();
            
            // Fetch the newly created company
            logger.info("Recuperando la empresa recién creada");
            Company company = companyRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("Error al recuperar la empresa recién creada para ID: {}", userId);
                        return new RuntimeException("Error al recuperar la empresa recién creada");
                    });
            
            // Create DTO for messaging
            CompanyDTO companyDTO = new CompanyDTO(
                company.getId(),
                company.getUsername(),
                company.getNit(),
                company.getName(),
                company.getSector(),
                company.getContactPhone(),
                company.getContactFirstName(),
                company.getContactLastName(),
                company.getContactPosition()
            );
            
            logger.info("Enviando notificación a la cola de mensajes");
            try {
                registrationSender.sendCompanyRegistration(companyDTO);
                logger.info("Notificación enviada correctamente");
            } catch (Exception e) {
                // No hacemos fallar la transacción si falla el envío de mensaje
                logger.warn("No se pudo enviar la notificación a la cola: {}", e.getMessage(), e);
            }
            
            logger.info("Empresa registrada exitosamente: {}", company.getName());
            return company;
        } catch (Exception e) {
            logger.error("Error en registerCompany: {}", e.getMessage(), e);
            throw new RuntimeException("Error al registrar empresa: " + e.getMessage(), e);
        }
    }

    public Optional<Company> findByNit(String nit) {
        return companyRepository.findByNit(nit);
    }
}