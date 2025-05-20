package com.example.coordination.messaging;

import com.example.coordination.dto.ProjectEvent;
import com.example.coordination.entity.Project;
import com.example.coordination.entity.ProjectStateEnum;
import com.example.coordination.repository.ProjectRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ProjectEventListener {

    private final ProjectRepository projectRepository;

    public ProjectEventListener(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @RabbitListener(queues = "project.events.cristobal.queue")
    public void handleProjectEvent(ProjectEvent event) {
        System.out.println("Proyecto recibido desde el micro de proyectos:");
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
                event.getStartDate(),
                ProjectStateEnum.valueOf(event.getStatus()) // Asegúrate que el enum coincida
        );

        // Guardar en base de datos local del microservicio de coordinación
        projectRepository.save(project);
    }
}