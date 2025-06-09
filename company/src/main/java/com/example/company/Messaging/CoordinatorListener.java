package com.example.company.Messaging;

import com.example.company.dto.ProjectDTO;
import com.example.company.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CoordinatorListener {

    private final ProjectService projectService;
    private final ObjectMapper mapper;

    public CoordinatorListener(ProjectService projectService) {
        this.projectService = projectService;
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Manejo de LocalDate
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Evita [yyyy,MM,dd]
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignora campos extra
    }

    @RabbitListener(queues = "coordinator.to.company.queue")
    public void receiveProjectStatus(String message) {
        System.out.println("📩 Mensaje JSON recibido desde coordinator:");
        System.out.println(message);

        try {
            ProjectDTO dto = mapper.readValue(message, ProjectDTO.class);
            projectService.updateProjectStatus(dto);
            System.out.println(" Estado actualizado: " + dto.getId() + " → " + dto.getState());
        } catch (Exception e) {
            System.err.println(" Error al deserializar ProjectDTO: " + e.getMessage());
        }
    }
}
