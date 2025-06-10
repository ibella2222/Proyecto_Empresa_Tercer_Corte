package com.example.student.Domain.Service;

import com.example.student.Domain.Model.Student;
import com.example.student.Domain.Port.In.StudentUseCase;
import com.example.student.Domain.Port.Out.StudentRepositoryPort;
import com.example.student.Infrastructure.Config.RabbitMQConfig;
import com.example.student.Infrastructure.Dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements StudentUseCase {

    private final StudentRepositoryPort studentRepositoryPort;
    private final RabbitTemplate rabbitTemplate; // <-- 1. Añadir RabbitTemplate
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    // 2. Actualizar el constructor para que reciba RabbitTemplate
    public StudentService(StudentRepositoryPort studentRepositoryPort, RabbitTemplate rabbitTemplate) {
        this.studentRepositoryPort = studentRepositoryPort;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Student applyToProject(String studentId, String projectId) {
        if (studentRepositoryPort.existsByIdAndProjectId(studentId, projectId)) {
            throw new IllegalStateException("Ya estás postulado a este proyecto.");
        }

        Student student = studentRepositoryPort.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Estudiante no encontrado."));

        student.setProjectId(projectId);
        return studentRepositoryPort.save(student);
    }

    @Override
    public void cancelApplication(String studentId, String projectId) {
        Student student = studentRepositoryPort.findById(studentId)
                .filter(s -> projectId.equals(s.getProjectId()))
                .orElseThrow(() -> new IllegalStateException("No se encontró la postulación para cancelar."));

        student.setProjectId(null);
        studentRepositoryPort.save(student);
    }

    @Override
    public Optional<Student> getStudentById(String studentId) {
        return studentRepositoryPort.findById(studentId);
    }

    @Override
    public List<Student> getStudentsByProjectId(String projectId) {
        return studentRepositoryPort.findByProjectId(projectId);
    }

    @Override
    public Student updateStudentProfile(String studentId, String firstName, String lastName, String program) {
        Student student = studentRepositoryPort.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Estudiante no encontrado."));

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setProgram(program);

        return studentRepositoryPort.save(student);
    }
    @Override
    public Student createOrUpdateProfile(String studentId, String username, String firstName, String lastName, String program) {
        Student student = studentRepositoryPort.findById(studentId)
                .orElse(new Student(studentId, username, firstName, lastName, program));

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setProgram(program);

        // Guardar en la base de datos
        Student savedStudent = studentRepositoryPort.save(student);
        logger.info("Perfil de estudiante guardado en la BD: {}", savedStudent.getUsername());

        // --- 3. ENVIAR EL MENSAJE DESPUÉS DE GUARDAR ---
        if (savedStudent != null) {
            StudentDTO studentDTO = toDto(savedStudent); // Convertir a DTO para el evento

            // Publicar el evento en RabbitMQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.STUDENT_EXCHANGE, RabbitMQConfig.STUDENT_ROUTING_KEY, studentDTO);

            logger.info("✅ Evento de estudiante enviado a RabbitMQ: {}", studentDTO);
        }
        // -------------------------------------------------

        return savedStudent;
    }
    private StudentDTO toDto(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getUsername(),
                student.getFirstName(),
                student.getLastName(),
                student.getProgram(),
                student.getProjectId()
        );
    }
}