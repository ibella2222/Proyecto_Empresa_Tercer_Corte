package com.example.coordination.adapter.messaging;

// --- Imports necesarios para el logging ---
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// --- Fin de imports para logging ---

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.coordination.dto.StudentDTO;

import java.util.*;

@Component
public class StudentListener {

    // --- PASO 1: Crear una instancia del Logger ---
    // Esto nos permitirá imprimir mensajes en la consola de forma estandarizada.
    private static final Logger logger = LoggerFactory.getLogger(StudentListener.class);

    // Mapa en memoria: projectId -> lista de estudiantes
    private final Map<String, List<StudentDTO>> projectStudents = new HashMap<>();

    /**
     * Este método se activa automáticamente cuando llega un mensaje
     * a la cola 'student.queue'.
     * @param studentDTO El objeto estudiante deserializado desde el mensaje JSON.
     */
    @RabbitListener(queues = "student.queue")
    public void receiveStudent(StudentDTO studentDTO) {
        // --- PASO 2: Añadir los mensajes de depuración ---

        // Mensaje #1: Confirma que el listener recibió algo.
        logger.info("✅ Mensaje recibido en la cola de estudiantes!");

        // Mensaje #2: Muestra el contenido completo del estudiante que llegó.
        // El .toString() en el DTO es útil aquí. Si no lo tienes, Lombok lo puede generar con @Data.
        logger.info("🎓 Datos del Estudiante: {}", studentDTO.toString());

        // Lógica de negocio (ya la tenías)
        String projectId = studentDTO.getProjectId();
        if (projectId != null) {
            projectStudents
                    .computeIfAbsent(projectId, k -> new ArrayList<>())
                    .add(studentDTO);

            // Mensaje #3: Confirma que el estudiante fue procesado y añadido al mapa.
            logger.info("👍 Estudiante '{}' añadido al proyecto '{}'.", studentDTO.getUsername(), projectId);
        } else {
            logger.warn("⚠️ El estudiante recibido no tiene un ID de proyecto asignado. No se puede procesar.");
        }
    }

    /**
     * Método para obtener los estudiantes de un proyecto (ya lo tenías).
     */
    public List<StudentDTO> getStudentsByProjectId(String projectId) {
        return projectStudents.getOrDefault(projectId, Collections.emptyList());
    }
}