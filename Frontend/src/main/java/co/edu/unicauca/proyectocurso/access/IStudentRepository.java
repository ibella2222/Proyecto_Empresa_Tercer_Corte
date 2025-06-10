package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Student;
import java.util.ArrayList;
import java.util.List;

public interface IStudentRepository {
    /**
     * Guarda un estudiante en la base de datos.
     * @param student Estudiante a registrar.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    boolean save(String username, Student student);


    /**
     * Obtiene la lista de todos los estudiantes registrados.
     * @return Lista de estudiantes.
     */
    List<Student> findAll();

    /**
     * Actualiza un estudiante existente.
     * @param student Estudiante a actualizar.
     * @return true si se actualizó correctamente, false en caso contrario.
     */
    boolean update(Student student);

    /**
     * Encuentra un estudiante por su username.
     * @param username Nombre de usuario del estudiante.
     * @return El estudiante encontrado o null si no existe.
     */
    Student findByUsername(String username);
    

    /**
     * Lista los estudiantes que están asociados a un proyecto específico.
     * @param projectId ID del proyecto.
     * @return Lista de estudiantes.
     */
    ArrayList<Student> findStudentsByProjectId(String projectId);

    /**
     * Verifica si un estudiante ya existe basado en su username.
     * @param username Nombre de usuario.
     * @return true si existe, false en caso contrario.
     */
    boolean studentExists(String username);
    Student getMyProfile();
    boolean applyToProject(String projectId);
    Student createOrUpdateProfile(String program);

}
