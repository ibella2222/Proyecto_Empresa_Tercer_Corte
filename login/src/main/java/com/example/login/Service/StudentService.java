package com.example.login.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.login.DTO.StudentDTO;
import com.example.login.Entities.Student;
import com.example.login.Entities.User;
import com.example.login.Messaging.StudentRegistrationSender;
import com.example.login.Repository.StudentRepository;
import com.example.login.Repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class StudentService {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentRegistrationSender registrationSender;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, StudentRegistrationSender registrationSender) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.registrationSender = registrationSender;
    }
    
    /**
     * Register a new student based on an existing user
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Student registerStudent(int userId, String firstName, String lastName, String program, String projectId) {
        try {
            logger.info("Iniciando registro de estudiante para userId: {}", userId);
            
            // Check if a student with this ID already exists
            if (studentRepository.existsById(userId)) {
                logger.warn("El usuario {} ya es un estudiante", userId);
                throw new RuntimeException("El usuario ya es un estudiante");
            }
            
            // Get the user with pessimistic locking
            logger.info("Buscando usuario con ID: {}", userId);
            User user = userRepository.findByIdWithLock(userId)
                    .orElseThrow(() -> {
                        logger.error("Usuario no encontrado con ID: {}", userId);
                        return new RuntimeException("Usuario no encontrado con ID: " + userId);
                    });
            
            logger.info("Usuario encontrado: {}", user.getUsername());
            
            // Create and populate a new Student directly with SQL
            logger.info("Insertando registro de estudiante en la base de datos");
            entityManager.createNativeQuery(
                "INSERT INTO students (user_id, first_name, last_name, program, project_id) VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, userId)
                .setParameter(2, firstName)
                .setParameter(3, lastName)
                .setParameter(4, program)
                .setParameter(5, projectId)
                .executeUpdate();
            
            // Clear persistence context to force a fresh load
            entityManager.flush();
            entityManager.clear();
            
            // Fetch the newly created student
            logger.info("Recuperando el estudiante recién creado");
            Student student = studentRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("Error al recuperar el estudiante recién creado para ID: {}", userId);
                        return new RuntimeException("Error al recuperar el estudiante recién creado");
                    });
            
            // Create DTO for messaging
            StudentDTO studentDTO = new StudentDTO(
                student.getId(),
                student.getUsername(),
                student.getFirstName(),
                student.getLastName(),
                student.getProgram(),
                student.getProjectId()
            );
            
            logger.info("Enviando notificación a la cola de mensajes");
            registrationSender.sendStudentRegistration(studentDTO);
            logger.info("Notificación enviada correctamente");
            
            return student;
        } catch (Exception e) {
            logger.error("Error en registerStudent: {}", e.getMessage(), e);
            throw new RuntimeException("Error al registrar estudiante: " + e.getMessage(), e);
        }
    }
}