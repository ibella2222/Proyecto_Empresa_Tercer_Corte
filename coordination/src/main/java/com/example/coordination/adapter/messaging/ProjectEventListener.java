package com.example.coordination.adapter.messaging;

import com.example.coordination.adapter.repository.ProjectRepository;
import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.dto.ProjectEvent;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ProjectEventListener {

    private final ProjectRepository projectRepository;

    public ProjectEventListener(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    @RabbitListener(queues = "company.project.queue")
    public void handleProjectEvent(ProjectEvent event) {
        System.out.println("Proyecto recibido desde el micro de empresa:");
        System.out.println("ID: " + event.getId());
        System.out.println("Nombre: " + event.getName());
        // ... demás prints ...

        // Convertir el evento a entidad Project
        Project project = new Project(
                event.getId(),
                event.getName(),
                event.getCompanyNIT(),
                event.getSummary(),
                event.getObjectives(),
                event.getDescription(),
                event.getMaxMonths(),
                event.getBudget(),
                event.getDate(),
                ProjectStateEnum.valueOf(event.getState()) // Asegúrate que el enum coincida
        );

        // Guardar en base de datos local del microservicio de coordinación
        projectRepository.save(project);
    }
}