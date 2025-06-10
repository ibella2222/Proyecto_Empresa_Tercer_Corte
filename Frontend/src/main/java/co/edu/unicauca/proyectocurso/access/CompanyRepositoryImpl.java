package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Company;
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

/**
 * Implementación del repositorio de Compañías que se comunica
 * con el microservicio a través del API Gateway de forma autenticada.
 */
public class CompanyRepositoryImpl implements ICompanyRepository {

    // Apuntamos a la ruta base del API Gateway para el coordinador
    private static final String BASE_URLb = "http://localhost:8081/coordinators/projects";
    private static final String BASE_URL = "http://localhost:8081/companies";

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public boolean save(Company company, String username) {
        try {
            // El endpoint en el backend para registrar una empresa.
            String url = BASE_URL + "/register/" + username;
            String json = gson.toJson(company);
            sendPostRequest(url, json);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo guardar la compañía. " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean approveProject(String projectId) {
        try {
            // Construimos la URL completa para aprobar
            String url = BASE_URLb + "/" + projectId + "/approve";
            sendPutRequest(url); // Usamos PUT para actualizaciones de estado
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo aprobar el proyecto. " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para enviar peticiones PUT (es como un POST sin cuerpo)
    private void sendPutRequest(String urlString) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json"); // Aunque no enviemos body, es buena práctica

        // Añadimos el token de autenticación del coordinador logueado
        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        int responseCode = con.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            throw new Exception("Error del servidor en PUT: " + responseCode);
        }
    }

    @Override
    public List<Company> findAll() {
        try {
            String url = BASE_URL + "/all";
            String responseJson = sendGetRequest(url);
            if (responseJson != null) {
                Type listType = new TypeToken<List<Company>>() {}.getType();
                return gson.fromJson(responseJson, listType);
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo obtener la lista de compañías. " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Company findByNIT(String nit) {
        try {
            // Este es el método crucial que estaba fallando.
            String url = BASE_URL + "/nit/" + nit;
            String responseJson = sendGetRequest(url);
            if (responseJson != null) {
                return gson.fromJson(responseJson, Company.class);
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo encontrar la compañía por NIT. " + e.getMessage());
        }
        // Devuelve null si no se encuentra o hay un error.
        return null;
    }

    @Override
    public boolean existsCompanyNIT(String nit) {
        try {
            String url = BASE_URL + "/check-nit/" + nit;
            String responseJson = sendGetRequest(url);
            if (responseJson != null) {
                // Suponiendo que el backend devuelve un JSON como {"exists": true}
                return responseJson.contains("true");
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo verificar la existencia del NIT. " + e.getMessage());
        }
        return false;
    }

    // MÉTODOS AUXILIARES UNIFICADOS USANDO HttpURLConnection

    private String sendGetRequest(String urlString) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        // CAMBIO CLAVE: Añadir el token de autenticación a TODAS las peticiones.
        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readResponse(con);
        } else {
            System.err.println("GET Request a " + urlString + " falló con código: " + responseCode);
            return null; // Devolver null si la petición no fue exitosa.
        }
    }

// En la clase CompanyRepositoryImpl.java

    private String sendPostRequest(String urlString, String jsonBody) throws Exception {

        // --- INICIO DEL CÓDIGO DE DEPURACIÓN ---
        System.out.println("\n--- DEBUG: Preparando Petición POST ---");
        System.out.println("URL Destino: " + urlString);
        System.out.println("Cuerpo JSON a Enviar: " + jsonBody);
        // --- FIN DEL CÓDIGO DE DEPURACIÓN ---

        HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json"); // Es buena práctica añadirlo también
        con.setDoOutput(true);

        String token = AuthTokenManager.getInstance().getJwtToken();
        if (token != null && !token.isEmpty()) {
            // --- INICIO DEL CÓDIGO DE DEPURACIÓN ---
            System.out.println("Token Encontrado. Añadiendo encabezado 'Authorization'.");
            System.out.println("Token (primeros 30 caracteres): Bearer " + token.substring(0, 30) + "...");
            // --- FIN DEL CÓDIGO DE DEPURACIÓN ---
            con.setRequestProperty("Authorization", "Bearer " + token);
        } else {
            // --- INICIO DEL CÓDIGO DE DEPURACIÓN ---
            System.err.println("ADVERTENCIA: No se encontró ningún token en AuthTokenManager.");
            // --- FIN DEL CÓDIGO DE DEPURACIÓN ---
        }

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = con.getResponseCode();

        // --- INICIO DEL CÓDIGO DE DEPURACIÓN ---
        System.out.println("Código de Respuesta Recibido: " + responseCode);
        // --- FIN DEL CÓDIGO DE DEPURACIÓN ---

        if (responseCode >= 200 && responseCode < 300) {
            return readResponse(con);
        } else {
            String errorBody = readErrorResponse(con); // Método auxiliar para leer el cuerpo del error
            System.err.println("Cuerpo del Error: " + errorBody);
            throw new Exception("POST Request a " + urlString + " falló con código: " + responseCode);
        }
    }

    // AÑADE ESTE NUEVO MÉTODO AUXILIAR PARA LEER ERRORES
    private String readErrorResponse(HttpURLConnection con) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (Exception e) {
            return "No se pudo leer el cuerpo del error.";
        }
    }

    // Asegúrate que tu método readResponse también exista
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