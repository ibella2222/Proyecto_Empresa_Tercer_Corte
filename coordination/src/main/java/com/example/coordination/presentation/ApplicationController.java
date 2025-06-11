package com.example.coordination.presentation;

import com.example.coordination.application.port.in.ApplicationUseCase;
import com.example.coordination.dto.ApplicationActionDTO;
import com.example.coordination.dto.ApplicationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinators/applications") // Ruta base para acciones sobre postulaciones
public class ApplicationController {

    private final ApplicationUseCase applicationUseCase;

    // Este constructor ahora solo necesita la dependencia que usa
    public ApplicationController(ApplicationUseCase applicationUseCase) {
        this.applicationUseCase = applicationUseCase;
    }

    // ESTE ES EL ENDPOINT QUE SOLUCIONA TU ERROR 404
    @GetMapping("/pending")
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<List<ApplicationDTO>> getPendingApplications() {
        List<ApplicationDTO> applications = applicationUseCase.getPendingApplications();
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<String> approveApplication(@RequestBody ApplicationActionDTO actionDTO) {
        try {
            applicationUseCase.approveApplication(actionDTO.getStudentId(), actionDTO.getProjectId());
            return ResponseEntity.ok("Postulación de estudiante aprobada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reject")
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<String> rejectApplication(@RequestBody ApplicationActionDTO actionDTO) {
        try {
            applicationUseCase.rejectApplication(actionDTO.getStudentId(), actionDTO.getProjectId());
            return ResponseEntity.ok("Postulación de estudiante rechazada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}