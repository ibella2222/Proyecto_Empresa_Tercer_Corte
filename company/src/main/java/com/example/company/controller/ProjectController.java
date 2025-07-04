package com.example.company.controller;

import com.example.company.dto.ProjectDTO;
import com.example.company.entity.Project;
import com.example.company.service.ProjectService;
import com.example.company.entity.ProjectStateEnum; // Importa tu enum
import java.util.stream.Collectors; // Asegúrate de tener este import
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.company.dto.CompanyDTO;
import com.example.company.entity.Company;
import org.springframework.http.ResponseEntity;
import java.util.Map;

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


    @GetMapping("/all")
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
    // En tu ProjectController (el que maneja /projects)

    @GetMapping("/status/ACCEPTED")
    public ResponseEntity<List<ProjectDTO>> getAcceptedProjects() {
        try {
            // Llama al servicio usando el .name() para convertir el enum a String
            List<Project> acceptedProjects = service.findProjectsByStatus(ProjectStateEnum.ACCEPTED.name());

            // Convierte la lista de entidades a una lista de DTOs para la respuesta
            List<ProjectDTO> dtos = acceptedProjects.stream()
                    .map(this::convertToDTO) // Asumiendo que tienes un método convertToDto
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            // Manejo de errores en caso de que algo falle
            return ResponseEntity.internalServerError().build();
        }
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
