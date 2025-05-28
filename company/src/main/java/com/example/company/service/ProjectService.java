package com.example.company.service;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.example.company.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class ProjectService {

    private final RabbitTemplate rabbitTemplate;
    private final ProjectRepository repository;
    private final ObjectMapper mapper;

    public ProjectService(RabbitTemplate rabbitTemplate, ProjectRepository repository) {
        this.rabbitTemplate = rabbitTemplate;
        this.repository = repository;

        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public List<Project> listAll() {
        return repository.findAll();
    }

    public Optional<Project> findById(UUID id) {
        return repository.findById(id);
    }

    public Project create(Project project) {
        System.out.println("name: " + project.getName());
        System.out.println("description: " + project.getDescription());
        System.out.println("date: " + project.getDate());
        System.out.println("companyNIT: " + project.getCompanyNIT());

        if (project.getName() == null || project.getDescription() == null ||
                project.getDate() == null || project.getCompanyNIT() == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar diligenciados.");
        }

        if (project.getId() == null) {
            project.setId(UUID.randomUUID());
        }

        project.setState("RECEIVED");

        Project saved = repository.save(project);

        try {
            String json = mapper.writeValueAsString(saved);
            rabbitTemplate.convertAndSend("company.exchange", "company.project.routingkey", json);
        } catch (Exception e) {
            System.err.println("❌ Error al serializar el proyecto: " + e.getMessage());
        }

        return saved;
    }

    public Project update(Project project) {
        Project existing = repository.findById(project.getId())
            .orElseThrow(() -> new IllegalArgumentException("El proyecto no existe."));

        if (!"RECEIVED".equals(existing.getState())) {
            throw new IllegalStateException("Solo se pueden editar proyectos en estado RECEIVED.");
        }

        project.setState(existing.getState());

        if (project.getCompanyNIT() == null) {
            project.setCompanyNIT(existing.getCompanyNIT());
        }

        return repository.save(project);
    }

    public void updateProjectStatus(ProjectDTO dto) {
        repository.findById(dto.getId()).ifPresent(project -> {
            project.setState(dto.getState());
            if (dto.getJustification() != null) {
                project.setJustification(dto.getJustification());
            }
            repository.save(project);
        });
    }

    public boolean delete(UUID id) {
        Optional<Project> projectOpt = repository.findById(id);
        if (projectOpt.isEmpty()) return false;

        Project project = projectOpt.get();
        if ("IN_EXECUTION".equals(project.getState())) {
            throw new IllegalStateException("No se puede eliminar un proyecto en ejecución.");
        }

        repository.deleteById(id);
        return true;
    }

    public List<Project> findByCompanyNIT(String nit) {
        return repository.findByCompanyNIT(nit);
    }
}
