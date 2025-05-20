package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.StudentProject;
import co.edu.unicauca.proyectocurso.domain.entities.Student;
import co.edu.unicauca.proyectocurso.domain.entities.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class StudentProjectRepositoryImpl implements IStudentProjectRepository {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8082/api/student-projects"; // asumimos que los maneja el coordinator

    public StudentProjectRepositoryImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean save(StudentProject studentProject, String studentID, String projectID) {
        try {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", studentProject.getId());
            dto.put("studentId", studentID);
            dto.put("projectId", projectID);
            dto.put("status", studentProject.getStatus().toString());

            ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, dto, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error al guardar asignación estudiante-proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<StudentProject> findAll() {
        try {
            ResponseEntity<StudentProject[]> response = restTemplate.getForEntity(baseUrl, StudentProject[].class);
            return response.getBody() != null ? Arrays.asList(response.getBody()) : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al obtener asignaciones: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean update(StudentProject studentProject) {
        try {
            String url = baseUrl + "/" + studentProject.getId();
            restTemplate.put(url, studentProject);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar asignación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(UUID studentProjectID) {
        try {
            String url = baseUrl + "/" + studentProjectID;
            restTemplate.delete(url);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar asignación: " + e.getMessage());
            return false;
        }
    }
}
