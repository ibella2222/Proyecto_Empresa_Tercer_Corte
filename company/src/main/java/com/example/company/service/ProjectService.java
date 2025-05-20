package com.example.company.service;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<String, Project> db = new HashMap<>();

    public ProjectService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Project> listAll() {
        return new ArrayList<>(db.values());
    }

    public Optional<Project> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    public Project create(Project project) {
        if (project.getName() == null || project.getSummary() == null ||
                project.getDate() == null || project.getCompanyNIT() == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar diligenciados.");
        }

        // Validar formato de fecha básico (ISO 8601: yyyy-MM-dd)
        if (!project.getDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("El formato de la fecha debe ser YYYY-MM-DD.");
        }

        // Asignar UUID si no está presente
        if (project.getId() == null || project.getId().isBlank()) {
            project.setId(UUID.randomUUID().toString());
        }

        project.setState("RECEIVED");
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

        // Mantener el estado y NIT si no fueron actualizados
        project.setState(existing.getState());
        if (project.getCompanyNIT() == null) {
            project.setCompanyNIT(existing.getCompanyNIT());
        }

        db.put(project.getId(), project);
        return project;
    }

    public void updateProjectStatus(ProjectDTO dto) {
        if (db.containsKey(dto.getId())) {
            Project p = db.get(dto.getId());
            p.setState(dto.getState());
            if (dto.getJustification() != null) {
                p.setJustification(dto.getJustification());
            }
        }
    }

    public boolean delete(String id) {
        Project project = db.get(id);
        if (project == null) return false;
        if ("IN_EXECUTION".equals(project.getState())) {
            throw new IllegalStateException("No se puede eliminar un proyecto en ejecución.");
        }
        return db.remove(id) != null;
    }
}
