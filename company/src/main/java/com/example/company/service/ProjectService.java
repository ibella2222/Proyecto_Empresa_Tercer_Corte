package com.example.company.service;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<UUID, Project> db = new HashMap<>();

    public ProjectService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Project> listAll() {
        return new ArrayList<>(db.values());
    }

    public Optional<Project> findById(UUID id) {
        return Optional.ofNullable(db.get(id));
    }

    public Project create(Project project) {
        // Validación de campos obligatorios
        if (project.getTitle() == null || project.getDescription() == null ||
                project.getStartDate() == null || project.getCompanyNit() == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar diligenciados.");
        }

        // Validar formato de fecha básica (ISO 8601: yyyy-MM-dd)
        if (!project.getStartDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("El formato de la fecha debe ser YYYY-MM-DD.");
        }

        // Generar UUID automáticamente si no viene
        if (project.getId() == null) {
            project.setId(UUID.randomUUID());
        }

        project.setStatus("RECEIVED");
        db.put(project.getId(), project);

        // Enviar mensaje a RabbitMQ
        rabbitTemplate.convertAndSend("company.exchange", "company.routingkey", project);
        return project;
    }

    public Project update(Project project) {
        Project existing = db.get(project.getId());
        if (existing == null) {
            throw new IllegalArgumentException("El proyecto no existe.");
        }

        if (!"RECEIVED".equals(existing.getStatus())) {
            throw new IllegalStateException("Solo se pueden editar proyectos en estado RECEIVED.");
        }

        // Conservar el estado y el NIT si no vienen
        project.setStatus(existing.getStatus());
        if (project.getCompanyNit() == null) {
            project.setCompanyNit(existing.getCompanyNit());
        }

        db.put(project.getId(), project);
        return project;
    }

    public void updateProjectStatus(ProjectDTO dto) {
        Project project = db.get(dto.getId());
        if (project != null) {
            project.setStatus(dto.getStatus());
            if (dto.getJustification() != null) {
                project.setJustification(dto.getJustification());
            }
        }
    }

    public boolean delete(UUID id) {
        Project project = db.get(id);
        if (project == null) return false;
        if ("IN_EXECUTION".equals(project.getStatus())) {
            throw new IllegalStateException("No se puede eliminar un proyecto en ejecución.");
        }
        return db.remove(id) != null;
    }
}
