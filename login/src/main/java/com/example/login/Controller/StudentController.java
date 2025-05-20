package com.example.login.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login.Config.RabbitMQStudentConfig;
import com.example.login.DTO.StudentDTO;
import com.example.login.Entities.Student;
import com.example.login.Entities.User;
import com.example.login.Repository.StudentRepository;
import com.example.login.Service.StudentService;
import com.example.login.Service.UserService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

        // Inyectar RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Registra un estudiante basándose en un usuario ya existente.
     * @param userId ID del usuario ya registrado.
     * @param studentDTO datos adicionales del estudiante.
     * @return ResponseEntity con resultado del registro.
     */
    @PostMapping("/register/{userId}")
    public ResponseEntity<?> registerStudent(@PathVariable int userId, @RequestBody StudentDTO studentDTO) {
        try {
            Optional<User> optionalUser = userService.findById(userId);
    
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
    
            User user = optionalUser.get();
    
            // Validar que tenga rol de estudiante
            if (!"ESTUDIANTE".equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario no tiene el rol de ESTUDIANTE"));
            }
    
            // Verificar que aún no esté registrado como estudiante
            if (studentRepository.existsById(user.getId())) {
                return ResponseEntity.status(409).body(Map.of("error", "El usuario ya está registrado como estudiante"));
            }
    
// Registrar estudiante
Student savedStudent = studentService.registerStudent(
    user.getId(),  // ✅ Este es el ID que espera el método
    studentDTO.getFirstName(),
    studentDTO.getLastName(),
    studentDTO.getProgram(),
    studentDTO.getProjectId()
);

            rabbitTemplate.convertAndSend(
                RabbitMQStudentConfig.STUDENT_EXCHANGE,
                RabbitMQStudentConfig.STUDENT_ROUTING_KEY,
                studentDTO
            );

    
            return ResponseEntity.ok(Map.of(
                "message", "Estudiante registrado correctamente",
                "studentId", savedStudent.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error al registrar estudiante: " + e.getMessage()
            ));
        }
    }
    /**
     * Obtiene los datos de un estudiante por su username.
     * Se utiliza el método findByUsername de la capa de servicio para obtener
     * tanto la información del usuario como la información adicional del estudiante.
     * @param username El nombre de usuario del estudiante.
     * @return Datos del estudiante si existe, o error en caso contrario.
     */
    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> getStudentByUsername(@PathVariable String username) {
        try {
            Optional<User> student = studentRepository.findByUsername(username);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Estudiante no encontrado"));
            }
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el estudiante: " + e.getMessage()));
        }
    }

    /**
     * Verifica si un estudiante ya está registrado en el sistema según su username.
     * @param username El nombre de usuario a verificar.
     * @return Un mapa con el estado de existencia.
     */

       /**
     * Obtiene la información del estudiante basado en el ID del usuario.
     * Esta URL se corresponde con la construcción del endpoint:
     * studentServiceUrl + "/user/" + userId
     * 
     * Ejemplo: GET /api/students/user/123
     *
     * @param userId ID del usuario.
     * @return Datos del estudiante o mensaje de error si no se encuentra.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getStudentByUserId(@PathVariable int userId) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(userId);
            if (optionalStudent.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Estudiante no encontrado para el ID: " + userId));
            }
            return ResponseEntity.ok(optionalStudent.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el estudiante: " + e.getMessage()));
        }
    }

@GetMapping("/exists/{username}")
public ResponseEntity<Map<String, Object>> studentExists(@PathVariable String username) {
    try {
        boolean exists = studentRepository.existsByUsername(username); // Verifica si el estudiante existe en el repositorio
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        // Si ocurre un error, se captura la excepción y se devuelve un mensaje de error
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Error al verificar existencia: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

}
