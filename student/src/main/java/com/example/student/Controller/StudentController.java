package com.example.student.Controller;


import com.example.student.Entities.Student;
import com.example.student.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Obtiene todos los estudiantes
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    /**
     * Obtiene un estudiante por su username
     */
    @GetMapping("/{username}")
    public ResponseEntity<Student> getStudentByUsername(@PathVariable String username) {
        return studentRepository.findById(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene estudiantes por el ID del proyecto asignado
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Student>> getStudentsByProject(@PathVariable String projectId) {
        List<Student> students = studentRepository.findByProjectId(projectId);
        return ResponseEntity.ok(students);
    }

    /**
     * Crea un nuevo estudiante, validando que no esté ya postulado al mismo proyecto
     */
    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        boolean exists = studentRepository.findAll().stream()
                .anyMatch(s -> s.getUsername().equals(student.getUsername()) &&
                        projectIdEquals(s.getProjectId(), student.getProjectId()));

        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya estás postulado a este proyecto.");
        }

        studentRepository.save(student);
        return ResponseEntity.ok("Postulación exitosa");
    }

    /**
     * Actualiza un estudiante existente
     */
    @PutMapping("/{username}")
    public ResponseEntity<Student> updateStudent(@PathVariable String username, @RequestBody Student student) {
        if (!studentRepository.existsById(username)) {
            return ResponseEntity.notFound().build();
        }

        student.setUsername(username);
        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Elimina un estudiante por username
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String username) {
        if (!studentRepository.existsById(username)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(username);
        return ResponseEntity.noContent().build();
    }

    /**
     * Asigna un proyecto a un estudiante
     */
    @PatchMapping("/{username}/project/{projectId}")
    public ResponseEntity<Student> assignProjectToStudent(@PathVariable String username, @PathVariable String projectId) {
        return studentRepository.findById(username)
                .map(student -> {
                    student.setProjectId(projectId);
                    return ResponseEntity.ok(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cancela la postulación de un estudiante a un proyecto
     */
    @DeleteMapping("/{username}/projects/{projectId}")
    public ResponseEntity<String> cancelApplication(@PathVariable String username, @PathVariable String projectId) {
        Student student = studentRepository.findById(username).orElse(null);

        if (student == null || !projectId.equals(student.getProjectId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la postulación");
        }

        student.setProjectId(null);
        studentRepository.save(student);
        return ResponseEntity.ok("Postulación cancelada correctamente");
    }

    /**
     * Compara dos IDs de proyecto permitiendo nulls
     */
    private boolean projectIdEquals(String p1, String p2) {
        return (p1 == null && p2 == null) || (p1 != null && p1.equals(p2));
    }
}
