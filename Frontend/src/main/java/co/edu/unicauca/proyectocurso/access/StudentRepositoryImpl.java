package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import org.springframework.web.client.RestClientException;

public class StudentRepositoryImpl implements IStudentRepository {

    private final RestTemplate restTemplate;
    private final String studentServiceUrl = "http://localhost:8080/api/students";

    public StudentRepositoryImpl() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Guarda un nuevo estudiante (requiere username externo)
     * @param username
     */
    @Override
    public boolean save(String username, Student student) {
        try {
            String url = studentServiceUrl + "/register/" + username;

            Map<String, Object> studentDTO = new HashMap<>();
            studentDTO.put("firstName", student.getFirstName());
            studentDTO.put("lastName", student.getLastName());
            studentDTO.put("program", student.getProgram());
            studentDTO.put("projectId", student.getProjectId());

            ResponseEntity<Map> response = restTemplate.postForEntity(url, studentDTO, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            System.out.println("Error al guardar estudiante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método alternativo para registrar un estudiante directamente por campos
     * @param username
     * @param firstName
     * @param lastName
     * @param program
     * @param projectId
     * @return 
     */
    public boolean registerStudent(String username, String firstName, String lastName, String program, String projectId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = studentServiceUrl + "/register/" + username;

            Map<String, Object> studentDTO = new HashMap<>();
            studentDTO.put("firstName", firstName);
            studentDTO.put("lastName", lastName);
            studentDTO.put("program", program);
            studentDTO.put("projectId", (projectId != null && !projectId.isEmpty()) ? projectId : null);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(studentDTO);

            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType("application/json");

            HttpPost request = new HttpPost(url);
            request.setEntity(entity);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                return statusCode == 200 || statusCode == 201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar estudiante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los estudiantes
     */
    @Override
    public List<Student> findAll() {
        try {
            ResponseEntity<Student[]> response = restTemplate.getForEntity(studentServiceUrl, Student[].class);
            return response.getBody() != null ? Arrays.asList(response.getBody()) : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Encuentra un estudiante por su username
     */
    public Student findByUsername(String username) {
        try {
            String url = studentServiceUrl + "/user/" + username;
            return restTemplate.getForObject(url, Student.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista estudiantes por projectId
     */
    @Override
    public ArrayList<Student> findStudentsByProjectId(String projectId) {
        try {
            String url = studentServiceUrl + "/project/" + projectId;
            ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
            return response.getBody() != null ? new ArrayList<>(Arrays.asList(response.getBody())) : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Actualiza un estudiante (requiere username externo)
     */
    public boolean update(String username, Student student) {
        try {
            String url = studentServiceUrl + "/user/" + username;
            restTemplate.put(url, student);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si un estudiante existe por su username
     */
    public boolean studentExists(String username) {
        try {
            String url = studentServiceUrl + "/exists/" + username;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null) {
                return Boolean.TRUE.equals(response.getBody().get("exists"));
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean save(Student student) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update(Student student) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
