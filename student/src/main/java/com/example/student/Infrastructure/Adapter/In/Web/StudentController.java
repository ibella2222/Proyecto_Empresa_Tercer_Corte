package com.example.student.Infrastructure.Adapter.In.Web;

import com.example.student.Domain.Model.Student;
import com.example.student.Domain.Port.In.StudentUseCase;
import com.example.student.Infrastructure.Dto.StudentDTO;
import com.example.student.Infrastructure.Dto.UpdateProfileRequestDTO;// Necesitarás crear este DTO
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentUseCase studentUseCase;

    public StudentController(StudentUseCase studentUseCase) {
        this.studentUseCase = studentUseCase;
    }

    // Un estudiante se postula a un proyecto
    @PostMapping("/apply/{projectId}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<String> applyToProject(@PathVariable String projectId, @AuthenticationPrincipal Jwt jwt) {
        String studentId = jwt.getSubject(); // ¡Aquí está la magia! Obtenemos el 'sub' de Keycloak.
        try {
            studentUseCase.applyToProject(studentId, projectId);
            return ResponseEntity.ok("Postulación exitosa.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Un estudiante cancela su postulación
    @DeleteMapping("/cancel/{projectId}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<String> cancelApplication(@PathVariable String projectId, @AuthenticationPrincipal Jwt jwt) {
        String studentId = jwt.getSubject();
        try {
            studentUseCase.cancelApplication(studentId, projectId);
            return ResponseEntity.ok("Postulación cancelada correctamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener perfil del estudiante autenticado
    @GetMapping("/me")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<StudentDTO> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String studentId = jwt.getSubject();
        return studentUseCase.getStudentById(studentId)
                .map(this::toDto) // Convertir Student de dominio a StudentDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener estudiantes por ID de proyecto (para vistas de coordinador o empresa)
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('coordinator', 'company')")
    public ResponseEntity<List<StudentDTO>> getStudentsByProject(@PathVariable String projectId) {
        List<StudentDTO> students = studentUseCase.getStudentsByProjectId(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(students);
    }

    // Mapeador simple de Dominio a DTO
    private StudentDTO toDto(Student student) {
        return new StudentDTO(student.getId(), student.getUsername(), student.getFirstName(), student.getLastName(), student.getProgram(), student.getProjectId());
    }

    // infrastructure/adapter/in/web/StudentController.java
    // ... (resto de la clase StudentController)

    @PostMapping("/me/profile")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<StudentDTO> createOrUpdateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateProfileRequestDTO profileRequest) { // Usamos el DTO simplificado

        // 1. Extraer todos los datos necesarios del token JWT
        String studentId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");

        // --- INICIO DE LA REFACTORIZACIÓN ---
        // Extraemos nombre y apellido de los 'claims' estándar de OpenID Connect
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        // --- FIN DE LA REFACTORIZACIÓN ---

        // 2. Llamar al caso de uso con todos los datos (del token y del body)
        Student updatedStudent = studentUseCase.createOrUpdateProfile(
                studentId,
                username,
                firstName,  // Dato del token
                lastName,   // Dato del token
                profileRequest.getProgram() // Dato del body
        );

        // 3. Devolver la respuesta
        return ResponseEntity.ok(toDto(updatedStudent));
    }

// ... (resto de la clase)
}