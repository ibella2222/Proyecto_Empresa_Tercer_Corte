package com.example.company.Messaging;

import com.example.company.dto.ProjectDTO;
import com.example.company.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class CoordinatorListener {

    private final ProjectService projectService;
    private final ObjectMapper mapper;

    public CoordinatorListener(ProjectService projectService) {
        this.projectService = projectService;
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy")); //  Formato de fecha esperado
    }

    // 🟢 Ahora escucha la cola dedicada a proyectos
    @RabbitListener(queues = "company.project.queue")
    public void receiveProjectStatus(String message) {
        try {
            ProjectDTO dto = mapper.readValue(message, ProjectDTO.class);
            projectService.updateProjectStatus(dto);
            System.out.println(" Estado actualizado desde coordinator: " + dto.getId() + " -> " + dto.getState());
        } catch (Exception e) {
            System.err.println(" Error al deserializar mensaje de coordinator: " + e.getMessage());
        }
    }
}
