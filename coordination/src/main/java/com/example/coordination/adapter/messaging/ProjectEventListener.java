package com.example.coordination.adapter.messaging;

import com.example.coordination.adapter.repository.ProjectRepository;
import com.example.coordination.adapter.repository.ProjectStatsRepository;
import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.domain.model.ProjectStats;
import com.example.coordination.dto.ProjectDTO;

import java.time.LocalDate;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ProjectEventListener {
    private final ProjectStatsRepository projectStatsRepository;

    private final ProjectRepository projectRepository;

    public ProjectEventListener(ProjectRepository projectRepository, ProjectStatsRepository projectStatsRepository) {
        this.projectRepository = projectRepository;
        this.projectStatsRepository = projectStatsRepository;
    }

    @Transactional
    @RabbitListener(queues = "company.project.queue")
    public void handleProjectEvent(ProjectDTO event) {
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
        ProjectStats evento = new ProjectStats();
        evento.setProjectId(project.getId());
        evento.setState(project.getState());
        evento.setChangeDate(LocalDate.now());
        projectStatsRepository.save(evento);

        // Guardar en base de datos local del microservicio de coordinación
        projectRepository.save(project);
    }
}