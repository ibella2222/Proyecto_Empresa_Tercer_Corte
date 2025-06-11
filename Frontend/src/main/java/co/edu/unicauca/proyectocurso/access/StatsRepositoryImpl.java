package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.ProjectStats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatsRepositoryImpl implements IStatsRepository {

    // Apuntamos a la ruta de estadísticas a través del API GATEWAY
    private static final String BASE_URL = "http://localhost:8081/stats";

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public List<ProjectStats> findAll() {
        try {
            String url = BASE_URL + "/all";
            String responseJson = sendGetRequest(url);

            if (responseJson != null) {
                Type listType = new TypeToken<List<ProjectStats>>() {}.getType();
                return gson.fromJson(responseJson, listType);
            }
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo obtener la lista de estadísticas. " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS AUXILIARES ---

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
     * Implementación del método que pediste para leer el cuerpo de la respuesta.
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