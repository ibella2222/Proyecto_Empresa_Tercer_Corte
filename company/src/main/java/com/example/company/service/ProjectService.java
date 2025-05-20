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
        if (project.getName() == null || project.getDescription() == null ||
                project.getDate() == null || project.getCompanyNIT() == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar diligenciados.");
        }

        // Generar UUID automáticamente si no viene
        if (project.getId() == null) {
            project.setId(UUID.randomUUID());
        }

        // Estado inicial
        project.setState("RECEIVED");

        // Guardar en memoria
        db.put(project.getId(), project);

        // Enviar a RabbitMQ
        rabbitTemplate.convertAndSend("company.exchange", "company.routingkey", project);

        return project;
    }


    public Project update(Project project) {
        Project existing = db.get(project.getId());
        if (existing == null) {
            throw new IllegalArgumentException("El proyecto no existe.");
        }

        if (!"RECEIVED".equals(existing.getState())) {
            throw new IllegalStateException("Solo se pueden editar proyectos en estado RECEIVED.");
        }

        // Conservar el estado y el NIT si no vienen
        project.setState(existing.getState());
        if (project.getCompanyNIT() == null) {
            project.setCompanyNIT(existing.getCompanyNIT());
        }

        db.put(project.getId(), project);
        return project;
    }

    public void updateProjectStatus(ProjectDTO dto) {
        Project project = db.get(dto.getId());
        if (project != null) {
            project.setState(dto.getState());
            if (dto.getJustification() != null) {
                project.setJustification(dto.getJustification());
            }
        }
    }

    public boolean delete(UUID id) {
        Project project = db.get(id);
        if (project == null) return false;
        if ("IN_EXECUTION".equals(project.getState())) {
            throw new IllegalStateException("No se puede eliminar un proyecto en ejecución.");
        }
        return db.remove(id) != null;
    }
}
