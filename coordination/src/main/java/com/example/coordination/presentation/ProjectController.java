package com.example.coordination.presentation;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.coordination.adapter.repository.ProjectRepository;
import com.example.coordination.domain.service.ProjectService;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public ProjectController(ProjectRepository projectRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveProject(@PathVariable UUID id) {
        try {
            projectService.acceptProject(id);
            return ResponseEntity.ok("Proyecto aprobado correctamente.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error interno al aprobar el proyecto.");
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectProject(@PathVariable UUID id) {
        try {
            projectService.rejectProject(id);
            return ResponseEntity.ok("Proyecto rechazado correctamente con justificación.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error interno al rechazar el proyecto.");
        }
    }
}