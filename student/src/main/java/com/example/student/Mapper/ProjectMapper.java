package com.example.student.Mapper;

import com.example.student.DTO.ProjectDTO;
import com.example.student.Entities.Project;
import com.example.student.Entities.ProjectStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que se encarga de convertir entre la entidad Project y el DTO ProjectDTO
 */
public class ProjectMapper {

    /**
     * Convierte una entidad Project a un DTO ProjectDTO
     * @param project La entidad Project a convertir
     * @return Un objeto ProjectDTO con los datos de la entidad Project
     */
    public static ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO dto = new ProjectDTO();

        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setSummary(project.getSummary());
        dto.setObjectives(project.getObjectives());
        dto.setDescription(project.getDescription());
        dto.setMaxMonths(project.getMaxMonths());
        dto.setBudget(project.getBudget() != null ? project.getBudget().floatValue() : 0);
        dto.setDate(project.getStartDate());
        dto.setFinalizationDate(project.getFinalizationDate());
        dto.setCompanyNIT(project.getCompanyNIT());
        dto.setState(project.getStatus() != null ? project.getStatus().name() : "RECEIVED");
        dto.setComments(project.getComments() != null ?
                new ArrayList<>(project.getComments()) : new ArrayList<>());
        dto.setStudents(project.getStudents() != null ?
                new ArrayList<>(project.getStudents()) : new ArrayList<>());
        dto.setTasks(project.getTasks() != null ?
                new ArrayList<>(project.getTasks()) : new ArrayList<>());

        return dto;
    }

    /**
     * Convierte una lista de entidades Project a una lista de DTOs ProjectDTO
     * @param projects Lista de entidades Project a convertir
     * @return Lista de DTOs ProjectDTO
     */
    public static List<ProjectDTO> toDTOList(List<Project> projects) {
        if (projects == null) {
            return new ArrayList<>();
        }

        return projects.stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un DTO ProjectDTO a una entidad Project
     * @param dto El DTO ProjectDTO a convertir
     * @return Una entidad Project con los datos del DTO ProjectDTO
     */
    public static Project toEntity(ProjectDTO dto) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();

        project.setId(dto.getId());
        project.setName(dto.getName());
        project.setSummary(dto.getSummary());
        project.setObjectives(dto.getObjectives());
        project.setDescription(dto.getDescription());
        project.setMaxMonths(dto.getMaxMonths());
        project.setBudget(BigDecimal.valueOf(dto.getBudget()));
        project.setStartDate(dto.getDate());
        project.setFinalizationDate(dto.getFinalizationDate());
        project.setCompanyNIT(dto.getCompanyNIT());

        // Convertir String state a ProjectStatus enum
        try {
            project.setStatus(ProjectStatus.valueOf(dto.getState()));
        } catch (IllegalArgumentException e) {
            project.setStatus(ProjectStatus.RECEIVED); // Valor por defecto
        }

        project.setComments(dto.getComments() != null ?
                new ArrayList<>(dto.getComments()) : new ArrayList<>());
        project.setStudents(dto.getStudents() != null ?
                new ArrayList<>(dto.getStudents()) : new ArrayList<>());
        project.setTasks(dto.getTasks() != null ?
                new ArrayList<>(dto.getTasks()) : new ArrayList<>());

        return project;
    }
}