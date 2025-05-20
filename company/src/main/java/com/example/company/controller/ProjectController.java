package com.example.company.controller;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.example.company.service.ProjectService;
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

    @GetMapping
    public List<ProjectDTO> list() {
        return service.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<Project> get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public Project create(@RequestBody Project project) {
        return service.create(project);
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable UUID id, @RequestBody Project project) {
        project.setId(id);
        return service.update(project);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable UUID id) {
        return service.delete(id);
    }

    // Método auxiliar para convertir entidad a DTO
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setSummary(project.getSummary());
        dto.setObjectives(project.getObjectives());
        dto.setDescription(project.getDescription());
        dto.setDate(project.getDate()); // LocalDate
        dto.setFinalizationDate(project.getFinalizationDate()); // Date
        dto.setState(project.getState());
        dto.setCompanyNIT(project.getCompanyNIT());
        dto.setJustification(project.getJustification());
        dto.setBudget(project.getBudget());
        dto.setMaxMonths(project.getMaxMonths());
        return dto;
    }
}
