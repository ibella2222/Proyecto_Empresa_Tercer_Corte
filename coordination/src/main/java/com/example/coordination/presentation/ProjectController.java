package com.example.coordination.presentation;



import com.example.coordination.application.port.in.ProjectUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectUseCase projectUseCase;

    public ProjectController(ProjectUseCase projectUseCase) {
        this.projectUseCase = projectUseCase;
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveProject(@PathVariable UUID id) {
        try {
            projectUseCase.acceptProject(id);
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
            projectUseCase.rejectProject(id);
            return ResponseEntity.ok("Proyecto rechazado correctamente.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error interno al rechazar el proyecto.");
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<String> startExecution(@PathVariable UUID id) {
        try {
            projectUseCase.startExecution(id);
            return ResponseEntity.ok("Ejecución del proyecto iniciada.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error al iniciar ejecución del proyecto.");
        }
    }
}