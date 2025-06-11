package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.ApplicationDTO;
import co.edu.unicauca.proyectocurso.domain.entities.StudentProject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorRepositoryImpl implements ICoordinatorRepository {

    private static final String BASE_URL_APPLICATIONS = "http://localhost:8081/coordinators/applications";
    private static final String BASE_URL_PROJECTS = "http://localhost:8081/coordinators/projects";

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    // En CoordinatorRepositoryImpl.java
    @Override
    public List<ApplicationDTO> getPendingApplications() {
        try {
            String url = BASE_URL_APPLICATIONS + "/pending";
            String responseJson = sendGetRequest(url);
            if (responseJson != null) {
                // Deserializa directamente a una lista del DTO correcto
                Type listType = new TypeToken<List<ApplicationDTO>>() {}.getType();
                return gson.fromJson(responseJson, listType);
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo obtener la lista de postulaciones. " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean approveStudentApplication(String studentId, String projectId) {
        try {
            String url = BASE_URL_APPLICATIONS + "/approve";
            String jsonBody = String.format("{\"studentId\": \"%s\", \"projectId\": \"%s\"}", studentId, projectId);
            sendPostRequest(url, jsonBody);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo aprobar la postulación. " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rejectStudentApplication(String studentId, String projectId) {
        try {
            String url = BASE_URL_APPLICATIONS + "/reject";
            String jsonBody = String.format("{\"studentId\": \"%s\", \"projectId\": \"%s\"}", studentId, projectId);
            sendPostRequest(url, jsonBody);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo rechazar la postulación. " + e.getMessage());
            return false;
        }
    }

    // Este método es para aprobar el proyecto en sí, no la postulación de un estudiante
    public boolean approveProject(String projectId) {
        try {
            String url = BASE_URL_PROJECTS + "/" + projectId + "/approve";
            sendPutRequest(url);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo aprobar el proyecto. " + e.getMessage());
            return false;
        }
    }

    // --- INICIO DE LA IMPLEMENTACIÓN DE MÉTODOS AUXILIARES ---

    /**
     * Envía una petición GET autenticada a una URL.
     * @param urlString La URL completa del endpoint.
     * @return El cuerpo de la respuesta como un String, o null si falla.
     */
    private String sendGetRequest(String urlString) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readResponse(con);
        } else {
            System.err.println("GET Request a " + urlString + " falló con código: " + responseCode);
            return null;
        }
    }

    /**
     * Envía una petición POST autenticada con un cuerpo JSON.
     * @param urlString La URL completa del endpoint.
     * @param jsonBody El cuerpo de la petición en formato JSON.
     * @return El cuerpo de la respuesta como un String.
     */
    private String sendPostRequest(String urlString, String jsonBody) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

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
            throw new Exception("POST Request a " + urlString + " falló con código: " + responseCode);
        }
    }

    /**
     * Envía una petición PUT autenticada (usada para actualizaciones de estado).
     * @param urlString La URL completa del endpoint.
     */
    private void sendPutRequest(String urlString) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        int responseCode = con.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            throw new Exception("PUT Request a " + urlString + " falló con código: " + responseCode);
        }
    }

    /**
     * Lee el cuerpo de la respuesta de una conexión HTTP.
     * @param con La conexión activa.
     * @return El cuerpo de la respuesta como String.
     */
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