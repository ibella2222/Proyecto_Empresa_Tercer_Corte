package com.example.company.service;

import com.example.company.config.RabbitMQConfig;
import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.example.company.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
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

    @Transactional // Buena práctica para asegurar que el guardado y el envío sean atómicos
    public Project create(Project project) {
        if (project.getName() == null || project.getDescription() == null ||
                project.getDate() == null || project.getCompanyNIT() == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar diligenciados.");
        }

        if (project.getId() == null) {
            project.setId(UUID.randomUUID());
        }
        project.setState("RECEIVED");

        // 1. Guarda la entidad en la base de datos
        Project saved = repository.save(project);

        // 2. ⭐ LA CORRECCIÓN: Convierte la entidad guardada a un DTO
        ProjectDTO projectDTO = convertToDTO(saved);

        // 3. Envía el DTO a RabbitMQ
        try {
            System.out.println("🚀 Publicando nuevo proyecto DTO en la cola...");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,                // Nombre del Exchange
                    RabbitMQConfig.PROJECT_ROUTING_KEY,     // Routing Key
                    projectDTO                              // <-- ¡AHORA ENVÍAS EL DTO!
            );
            System.out.println("✅ Proyecto DTO publicado exitosamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al enviar el proyecto por RabbitMQ: " + e.getMessage());
            // Considera cómo manejar este error. ¿La creación debería fallar?
        }

        return saved; // Devuelves la entidad como antes
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
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setSummary(project.getSummary());
        dto.setObjectives(project.getObjectives());
        dto.setDescription(project.getDescription());
        dto.setDate(project.getDate());
        dto.setFinalizationDate(project.getFinalizationDate());
        dto.setState(project.getState());
        dto.setCompanyNIT(project.getCompanyNIT());
        dto.setJustification(project.getJustification());
        dto.setBudget(project.getBudget());
        dto.setMaxMonths(project.getMaxMonths());
        return dto;
    }

    public List<Project> findByCompanyNIT(String nit) {
        return repository.findByCompanyNIT(nit);
    }
}
