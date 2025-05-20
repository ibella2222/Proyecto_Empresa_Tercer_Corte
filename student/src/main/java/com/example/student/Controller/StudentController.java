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
     * Obtiene un estudiante por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return studentRepository.findById(id)
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
                .anyMatch(s -> s.getId() == student.getId() &&
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
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        student.setId(id);
        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Elimina un estudiante por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Asigna un proyecto a un estudiante
     */
    @PatchMapping("/{id}/project/{projectId}")
    public ResponseEntity<Student> assignProjectToStudent(@PathVariable int id, @PathVariable String projectId) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setProjectId(projectId);
                    return ResponseEntity.ok(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cancela la postulación de un estudiante a un proyecto
     */
    @DeleteMapping("/{studentId}/projects/{projectId}")
    public ResponseEntity<String> cancelApplication(@PathVariable int studentId, @PathVariable String projectId) {
        Student student = studentRepository.findById(studentId).orElse(null);

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
