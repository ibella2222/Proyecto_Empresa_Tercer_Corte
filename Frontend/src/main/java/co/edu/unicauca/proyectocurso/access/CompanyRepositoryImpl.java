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

    // ÚNICA URL BASE NECESARIA: la del servicio de compañías.
    private static final String BASE_URL = "http://localhost:8081/companies";

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public boolean save(Company company, String username) {
        try {
            String url = BASE_URL + "/register/" + username;
            String json = gson.toJson(company);
            sendPostRequest(url, json);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo guardar la compañía. " + e.getMessage());
            return false;
        }
    }

    // En la clase CompanyRepositoryImpl.java

    @Override
    public Company findByUsername(String username) {
        try {
            // Construye la URL correcta usando la BASE_URL
            String url = BASE_URL + "/user/" + username;

            // Llama al método auxiliar que ya sabe cómo enviar el token
            String responseJson = sendGetRequest(url);

            // Si la respuesta no es null (es decir, no fue un 404), la convierte a objeto
            if (responseJson != null) {
                return gson.fromJson(responseJson, Company.class);
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo encontrar la compañía por username. " + e.getMessage());
        }
        // Devuelve null si no se encuentra o si ocurre cualquier error
        return null;
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
            String url = BASE_URL + "/nit/" + nit;
            String responseJson = sendGetRequest(url);
            if (responseJson != null) {
                return gson.fromJson(responseJson, Company.class);
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo encontrar la compañía por NIT. " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean existsCompanyNIT(String nit) {
        try {
            String url = BASE_URL + "/check-nit/" + nit;
            String responseJson = sendGetRequest(url);
            return responseJson != null && responseJson.contains("true");
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo verificar la existencia del NIT. " + e.getMessage());
        }
        return false;
    }

    // --- MÉTODOS AUXILIARES ---

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