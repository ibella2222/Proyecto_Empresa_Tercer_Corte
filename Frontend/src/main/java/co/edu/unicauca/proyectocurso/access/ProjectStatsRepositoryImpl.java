/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.adapterDate.LocalDateAdapter;
import co.edu.unicauca.proyectocurso.domain.entities.ProjectStats;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Lenovo pc
 */
public class ProjectStatsRepositoryImpl implements IProjectStatsRepository{
    private static final String BASE_URL = "http://localhost:8084/stats";


    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();


    @Override
    public List<ProjectStats> findAll() {
        try {
            String response = sendGetRequest(BASE_URL + "/all");

            if (response == null || response.isEmpty()) {
                return new ArrayList<>();
            }

            // Usar Gson en lugar de Jackson
            java.lang.reflect.Type projectStatsListType = new com.google.gson.reflect.TypeToken<List<ProjectStats>>() {}.getType();
            return gson.fromJson(response, projectStatsListType);

        } catch (Exception e) {
            System.err.println("Error al obtener proyectos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String sendGetRequest(String url) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                return response.toString();
            }
        } else {
            System.err.println("GET " + url + " => " + responseCode);
            return null;
        }
    }
}
