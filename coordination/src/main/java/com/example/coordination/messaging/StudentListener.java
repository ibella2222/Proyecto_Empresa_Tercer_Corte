package com.example.coordination.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.coordination.dto.StudentDTO;


import java.util.*;

@Component
public class StudentListener {

    // Mapa en memoria: projectId -> lista de estudiantes
    private final Map<String, List<StudentDTO>> projectStudents = new HashMap<>();

    @RabbitListener(queues = "student.queue")
    public void receiveStudent(StudentDTO studentDTO) {
        System.out.println("🎓 Estudiante recibido desde RabbitMQ: " + studentDTO);
        
        // Agrega el estudiante al proyecto correspondiente
        projectStudents
                .computeIfAbsent(studentDTO.getProjectId(), k -> new ArrayList<>())
                .add(studentDTO);
    }

    public List<StudentDTO> getStudentsByProjectId(String projectId) {
        return projectStudents.getOrDefault(projectId, Collections.emptyList());
    }
}
