package com.example.company.controller;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.example.company.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    // Listar todos los proyectos como DTOs
    @GetMapping
    public List<ProjectDTO> list() {
        return service.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener un proyecto por ID
    @GetMapping("/{id}")
    public Optional<Project> get(@PathVariable UUID id) {
        return service.findById(id);
    }

    // Crear un nuevo proyecto
    @PostMapping
    public Project create(@Valid @RequestBody Project project) {
        return service.create(project);
    }

    // Actualizar un proyecto existente
    @PutMapping("/{id}")
    public Project update(@PathVariable UUID id, @Valid @RequestBody Project project) {
        project.setId(id);
        return service.update(project);
    }

    // Eliminar un proyecto
    @PostMapping("/delete/{id}")
    public boolean deleteViaPost(@PathVariable UUID id) {
        return service.delete(id);
    }

    // Obtener todos los proyectos por NIT de empresa
    @GetMapping("/company/{nit}")
    public List<ProjectDTO> getProjectsByCompanyNIT(@PathVariable String nit) {
        return service.findByCompanyNIT(nit).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Convertir una entidad Project a un DTO
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
}
