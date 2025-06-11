package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Student;
import co.edu.unicauca.proyectocurso.domain.entities.StudentProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.web.client.RestClientException;

public class StudentRepositoryImpl implements IStudentRepository {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8081/students";
    private final String studentServiceUrl = "http://localhost:8080/api/students";
    private final Gson gson = new GsonBuilder().create();

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

    @Override
    public Student getMyProfile() {
        try {
            // Llama al endpoint GET /students/me
            String responseJson = sendGetRequest(BASE_URL + "/me");
            if (responseJson == null) {
                // Esto sucede si el backend devuelve 404, indicando que el perfil no existe.
                return null;
            }
            return gson.fromJson(responseJson, Student.class);
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo obtener el perfil del estudiante. " + e.getMessage());
            return null;
        }
    }

    @Override
    public Student createOrUpdateProfile(String program) {
        try {
            // Llama al endpoint POST /students/me/profile
            String jsonBody = "{\"program\": \"" + program + "\"}";
            String responseJson = sendPostRequest(BASE_URL + "/me/profile", jsonBody);
            return gson.fromJson(responseJson, Student.class);
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo crear o actualizar el perfil. " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean applyToProject(String projectId) {
        try {
            // Llama al endpoint POST /students/apply/{projectId}
            // No se envía cuerpo (body), por eso el ""
            sendPostRequest(BASE_URL + "/apply/" + projectId, "");
            return true; // Si no hay excepción, la postulación fue exitosa.
        } catch (Exception e) {
            // El backend puede devolver 409 Conflict si ya está postulado.
            System.err.println("ERROR: No se pudo realizar la postulación. " + e.getMessage());
            return false;
        }
    }

    // MÉTODOS AUXILIARES PARA LAS PETICIONES HTTP

    private String sendGetRequest(String urlString) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        // Adjuntar el token de autorización
        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
            return readResponse(con);
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // 404 Not Found
            return null; // Devolvemos null para indicar que el recurso no existe
        } else {
            throw new Exception("Error del servidor en GET: " + responseCode + " " + con.getResponseMessage());
        }
    }

    private String sendPostRequest(String urlString, String jsonBody) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Adjuntar el token de autorización
        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = con.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            return readResponse(con);
        } else {
            throw new Exception("Error del servidor en POST: " + responseCode + " " + con.getResponseMessage());
        }
    }

    private String readResponse(HttpURLConnection con) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    
    
}
