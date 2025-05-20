
package com.example.company.Messaging;

import com.example.company.dto.ProjectDTO;
import com.example.company.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CoordinatorListener {

    private final ProjectService projectService;

    public CoordinatorListener(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RabbitListener(queues = "coordinator.to.company.queue")
    public void receiveProjectStatus(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProjectDTO dto = mapper.readValue(message, ProjectDTO.class);
            projectService.updateProjectStatus(dto);
            System.out.println("📥 Estado actualizado desde coordinator: " + dto.getId() + " -> " + dto.getState());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
