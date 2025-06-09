package co.edu.unicauca.proyectocurso.domain.services;

import co.edu.unicauca.proyectocurso.access.StudentRepositoryImpl;
import co.edu.unicauca.proyectocurso.domain.entities.Student;
import java.util.List;

public class StudentService extends Observado {
    private final StudentRepositoryImpl studentRepository;

    public StudentService(StudentRepositoryImpl repository) {
        this.studentRepository = repository;
    }

    public StudentService() {
        this.studentRepository = new StudentRepositoryImpl();
    }

    /**
     * Registra un nuevo estudiante
     */
    public boolean registerStudent(String username, String firstName, String lastName, String program, String projectId) {
        // Keycloak ya gestiona el username, así que solo se usa como referencia
        Student student = new Student(username, firstName, lastName, program, projectId);
        return studentRepository.save(username, student);  // <- Pasa el username por separado
    }

    /**
     * Lista todos los estudiantes registrados
     */
    public List<Student> listEstudiantes() {
        return studentRepository.findAll();
    }

    /**
     * Obtiene un estudiante por su username
     */
    public Student getStudentByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    /**
     * Lista estudiantes por ID de proyecto
     */
    public List<Student> listStudentsByProjectId(String projectId) {
        return studentRepository.findStudentsByProjectId(projectId);
    }

    /**
     * Verifica si un estudiante existe por su username
     */
    public boolean studentExists(String username) {
        return studentRepository.studentExists(username);
    }
}
