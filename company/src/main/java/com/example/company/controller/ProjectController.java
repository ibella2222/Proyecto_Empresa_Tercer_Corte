
package com.example.company.controller;


import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.example.company.entity.Company;
import com.example.company.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    // Retorna lista de ProjectDTOs para la GUI
    @GetMapping
    public List<ProjectDTO> list() {
        return service.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Resto de métodos sin cambios (puedes adaptarlos si deseas que también usen DTO)
    @GetMapping("/{id}")
    public Optional<Project> get(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping

    public Project create(@RequestBody Project project) {
        System.out.println("➡️ Proyecto recibido: " + project);
        try {
            return service.create(project);
        } catch (Exception e) {
            System.err.println(" Error al guardar proyecto: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow para que lo capture Spring también
        }
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable String id, @RequestBody Project project) {
        project.setId(id);
        return service.update(project);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return service.delete(id);
    }

    // Método auxiliar para convertir entidad a DTO
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setState(project.getState());
        dto.setJustification(project.getJustification());
        dto.setDescription(project.getDescription());
        dto.setCompanyNIT(project.getCompanyNIT());
        dto.setCompanyName(project.getCompanyName() != null ? project.getCompanyName() : "Desconocida");
        return dto;
    }
}

