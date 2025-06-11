package com.example.coordination.presentation;

import com.example.coordination.application.port.in.ProjectUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coordinators/projects") // Ruta base para acciones sobre proyectos
public class ProjectController {

    private final ProjectUseCase projectUseCase;

    // Este constructor ahora solo necesita la dependencia que usa
    public ProjectController(ProjectUseCase projectUseCase) {
        this.projectUseCase = projectUseCase;
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<String> approveProject(@PathVariable UUID id) {
        try {
            projectUseCase.acceptProject(id);
            return ResponseEntity.ok("Proyecto aprobado correctamente.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<String> rejectProject(@PathVariable UUID id) {
        try {
            projectUseCase.rejectProject(id);
            return ResponseEntity.ok("Proyecto rechazado correctamente.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<String> startExecution(@PathVariable UUID id) {
        try {
            projectUseCase.startExecution(id);
            return ResponseEntity.ok("Ejecución del proyecto iniciada.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}