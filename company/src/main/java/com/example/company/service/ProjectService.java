package com.example.company.service;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.company.adapter.LocalDateAdapter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProjectService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<UUID, Project> db = new HashMap<>();
    private final Gson gson;

    public ProjectService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // para fechas
                .create();
    }

    public List<Project> listAll() {
        return new ArrayList<>(db.values());
    }

    public Optional<Project> findById(UUID id) {
        return Optional.ofNullable(db.get(id));
    }

    public Project create(Project project) {
        System.out.println("name: " + project.getName());
        System.out.println("description: " + project.getDescription());
        System.out.println("date: " + project.getDate());
        System.out.println("companyNIT: " + project.getCompanyNIT());

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

        // Enviar como JSON por RabbitMQ a la cola de proyectos
        String json = gson.toJson(project);
        rabbitTemplate.convertAndSend("company.exchange", "company.project.routingkey", json);

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
