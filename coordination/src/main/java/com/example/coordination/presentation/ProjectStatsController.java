package com.example.coordination.presentation;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.coordination.domain.model.ProjectStats;
import com.example.coordination.domain.service.ProjectStatsService;


@RestController
@RequestMapping("/stats")
public class ProjectStatsController {

    private final ProjectStatsService service;

    public ProjectStatsController(ProjectStatsService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<?> list() {
        try {
            List<ProjectStats> projects = service.listAll();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            // Loguear el error (opcional)
            // logger.error("Error al obtener la lista de proyectos", e);
            
            // Retornar respuesta con status 500 y mensaje de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno al obtener la lista de proyectos");
        }
    }
}
