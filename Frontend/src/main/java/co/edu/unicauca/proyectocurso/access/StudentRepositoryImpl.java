package co.edu.unicauca.proyectocurso.access;

import org.springframework.http.ResponseEntity;
import co.edu.unicauca.proyectocurso.domain.entities.Project;
import co.edu.unicauca.proyectocurso.domain.entities.Student;
import co.edu.unicauca.proyectocurso.domain.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of the Student Repository interface
 */
public class StudentRepositoryImpl implements IStudentRepository {

    private final RestTemplate restTemplate;
    private final String userServiceUrl = "http://localhost:8080/api/auth";
    private final String studentServiceUrl = "http://localhost:8080/api/students"; 

    public StudentRepositoryImpl() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Registra un estudiante con los detalles proporcionados
     * @param username Nombre de usuario del estudiante
     * @param password Contraseña del estudiante
     * @param firstName Nombre del estudiante
     * @param lastName Apellido del estudiante
     * @param program Programa académico del estudiante
     * @param projectId ID del proyecto asociado (si aplica)
     * @param id ID del usuario
     * @return true si el registro es exitoso, false en caso contrario
     */
    public boolean registerStudent(String username, String password, String firstName, String lastName, String program, String projectId, int id) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // La URL correcta debe incluir el ID del usuario
            String url = studentServiceUrl + "/register/" + id;
            HttpPost request = new HttpPost(url);
            
            // Crear el objeto StudentDTO como lo espera el controlador
            Map<String, Object> studentDTO = new HashMap<>();
            studentDTO.put("firstName", firstName);
            studentDTO.put("lastName", lastName);
            studentDTO.put("program", program);
            
            // Solo agregar projectId si no es null o "null"
            if (projectId != null && !projectId.equals("null") && !projectId.isEmpty()) {
                studentDTO.put("projectId", projectId);
            } else {
                // Si no hay projectId, enviar null explícitamente
                studentDTO.put("projectId", null);
            }
            
            // Convertir a JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(studentDTO);
            
            System.out.println("Enviando datos: " + json);
            System.out.println("URL: " + url);
            
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType("application/json");
            
            request.setEntity(entity);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                
                // Imprimir respuesta para depuración
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                System.out.println("Código de estado: " + statusCode);
                System.out.println("Respuesta: " + responseBody);
                
                return statusCode == 200 || statusCode == 201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar estudiante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean save(Student student) {
        try {
            // Find user by username in the UserService
            String userUrl = userServiceUrl + "/user/" + student.getUsername();
            ResponseEntity<Map> userResponse = restTemplate.getForEntity(userUrl, Map.class);

            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                Map<String, Object> userMap = userResponse.getBody();
                Integer userId = (Integer) userMap.get("userId");
                
                if (userId == null) {
                    System.out.println("No se pudo obtener el ID del usuario");
                    return false;
                }
                
                // Usar el endpoint correcto para guardar el estudiante
                String url = studentServiceUrl + "/register/" + userId;
                
                // Crear el DTO necesario
                Map<String, Object> studentDTO = new HashMap<>();
                studentDTO.put("firstName", student.getFirstName());
                studentDTO.put("lastName", student.getLastName());
                studentDTO.put("program", student.getProgram());
                studentDTO.put("projectId", student.getProjectID());
                
                System.out.println("Enviando datos: " + studentDTO);
                System.out.println("URL: " + url);
                
                ResponseEntity<Map> response = restTemplate.postForEntity(url, studentDTO, Map.class);
                
                System.out.println("Respuesta: " + response.getBody());
                System.out.println("Código de estado: " + response.getStatusCode());
                
                return response.getStatusCode().is2xxSuccessful();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar estudiante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Student> findAll() {
        try {
            ResponseEntity<Student[]> response = restTemplate.getForEntity(studentServiceUrl, Student[].class);
            return response.getBody() != null ? Arrays.asList(response.getBody()) : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener todos los estudiantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Find a student by username
     * @param username The username to search for
     * @return The found Student object, or null if not found
     */
/**
 * Find a student by username
 * @param username The username to search for
 * @return The found Student object, or null if not found
 */
public Student findByUsername(String username) {
    try {
        // Primero verificamos si el estudiante existe
        if (!studentExists(username)) {
            System.out.println("Estudiante con username " + username + " no existe");
            return null;
        }
        
        System.out.println("Buscando estudiante con username: " + username);
        
        // Intentamos obtener la información del usuario primero
        String userUrl = userServiceUrl + "/user/" + username;
        ResponseEntity<Map> userResponse = restTemplate.getForEntity(userUrl, Map.class);
        
        if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
            System.out.println("No se pudo obtener información del usuario: " + username);
            return null;
        }
        
        // Extraemos el ID del usuario
        Map<String, Object> userData = userResponse.getBody();
        Integer userId = (Integer) userData.get("userId");
        String role = (String) userData.get("role");
        
        if (userId == null) {
            System.out.println("No se pudo obtener el ID del usuario");
            return null;
        }
        
        // Ahora buscamos la información del estudiante
        // Nota: Esto asume que tienes un endpoint para obtener estudiante por ID de usuario
        String studentUrl = studentServiceUrl + "/user/" + userId;
        System.out.println("URL para obtener estudiante: " + studentUrl);
        
        try {
            ResponseEntity<Map> studentResponse = restTemplate.getForEntity(studentUrl, Map.class);
            
            if (studentResponse.getStatusCode().is2xxSuccessful() && studentResponse.getBody() != null) {
                Map<String, Object> studentData = studentResponse.getBody();
                
                // Crear un nuevo objeto Student con los datos obtenidos
                Student student = new Student();
                student.setId(userId);
                student.setUsername(username);
                student.setRole(role);
                
                // Intentar extraer los campos específicos del estudiante
                if (studentData.containsKey("firstName")) {
                    student.setFirstName((String) studentData.get("firstName"));
                }
                
                if (studentData.containsKey("lastName")) {
                    student.setLastName((String) studentData.get("lastName"));
                }
                
                if (studentData.containsKey("program")) {
                    student.setProgram((String) studentData.get("program"));
                }
                
                if (studentData.containsKey("projectId")) {
                    student.setProjectID(studentData.get("projectId") != null ? 
                        studentData.get("projectId").toString() : null);
                }
                
                return student;
            } else {
                System.out.println("No se pudo obtener información del estudiante para el ID: " + userId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos del estudiante: " + e.getMessage());
            e.printStackTrace();
            
            // Intentamos crear un objeto Student básico con la información del usuario
            Student basicStudent = new Student();
            basicStudent.setId(userId);
            basicStudent.setUsername(username);
            basicStudent.setRole(role);
            return basicStudent;
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error al buscar estudiante por username: " + e.getMessage());
        return null;
    }
}

    @Override
    public boolean update(Student student) {
        try {
            String url = studentServiceUrl + "/" + student.getId();
            restTemplate.put(url, student);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al actualizar estudiante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Student findById(UUID studentId) {
        try {
            String url = studentServiceUrl + "/" + studentId;
            return restTemplate.getForObject(url, Student.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al buscar estudiante por ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Student> findStudentsByProjectId(String projectId) {
        try {
            String url = studentServiceUrl + "/project/" + projectId;
            ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
            return response.getBody() != null ? new ArrayList<>(Arrays.asList(response.getBody())) : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al buscar estudiantes por projectId: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Check if a student exists by username
     * @param username Username to check
     * @return true if student exists, false otherwise
     */
    public boolean studentExists(String username) {
        try {
            String url = studentServiceUrl + "/exists/" + username;
            
            // Cambiar el tipo de respuesta a Map para manejar la respuesta JSON
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseMap = response.getBody();
                // Obtener el valor de 'exists' del mapa de respuesta
                return Boolean.TRUE.equals(responseMap.get("exists"));
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al verificar si existe el estudiante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get student ID by username
     * @param username Username to search for
     * @return Student ID as a String
     */
    public String getStudentIdByUsername(String username) {
        try {
            String url = studentServiceUrl + "/id-by-username/" + username;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener ID de estudiante por username: " + e.getMessage());
            return null;
        }
    }
}