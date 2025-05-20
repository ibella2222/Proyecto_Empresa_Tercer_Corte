package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Company;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;


public class CompanyRepositoryImpl implements ICompanyRepository {

    // URL base para apuntar al controlador Spring Boot
    private static final String BASE_URL;

    static {
        String url = "http://localhost:8080/api/companies"; // Valor por defecto
        try {
            Properties props = new Properties();
            props.load(CompanyRepositoryImpl.class.getClassLoader().getResourceAsStream("config.properties"));
            String configUrl = props.getProperty("login.api");
            if (configUrl != null && !configUrl.trim().isEmpty()) {
                url = configUrl.trim();
            } else {
                System.out.println("⚠️ 'loguin.api' no está definido en config.properties. Usando valor por defecto.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar 'config.properties'. Usando URL por defecto.");
            e.printStackTrace();
        }
        BASE_URL = url;
    }

    
    private final HttpClient client;
    private final Gson gson;

    public CompanyRepositoryImpl() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    @Override
    public boolean existsCompanyNIT(String companyNIT) {
        // Endpoint para verificar si el NIT existe
        String url = BASE_URL + "/check-nit/" + companyNIT;

        // Realizar la petición HTTP GET
        String response = sendGetRequest(url);

        // Comprobar la respuesta, que debería ser un objeto JSON con un campo "exists"
        try {
            if (response != null) {
                ExistsResponse existsResponse = gson.fromJson(response, ExistsResponse.class);
                return existsResponse.exists;
            }
        } catch (Exception e) {
            System.err.println("Error al verificar NIT: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

@Override
public boolean save(Company company) {
    try {
        // Endpoint para registrar una empresa (usando el endpoint existente)
        String url = BASE_URL + "/register/" + company.getId(); // Use userId, not company.getId()
        
        // Crear DTO para enviar en formato JSON
        CompanyDTO companyDTO = new CompanyDTO(
            company.getNit(),
            company.getName(),
            company.getSector(),
            company.getContactPhone(),
            company.getContactFirstName(),
            company.getContactLastName(),
            company.getContactPosition()
        );
        
        // Convertir objeto a JSON
        String jsonData = gson.toJson(companyDTO);
        
        // Realizar la petición HTTP POST con datos JSON
        // Asegurar que se está enviando el Content-Type: application/json
        String response = sendPostRequestJson(url, jsonData);
        
        if (response != null) {
            SaveResponse saveResponse = gson.fromJson(response, SaveResponse.class);
            // Si hay un mensaje de éxito, la empresa se guardó correctamente
            return saveResponse.message != null && saveResponse.message.contains("registrada correctamente");
        }
    } catch (Exception e) {
        System.err.println("Error al guardar empresa: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

// Método para enviar solicitud POST con datos JSON
private String sendPostRequestJson(String url, String jsonData) {
    try {
        // Crear conexión HTTP
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        
        // Establecer encabezados para enviar JSON
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        
        // Enviar datos JSON
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Leer respuesta
        int responseCode = connection.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            // Respuesta exitosa
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            // Error en la respuesta
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.err.println("Error HTTP " + responseCode + ": " + response.toString());
                return null;
            }
        }
    } catch (Exception e) {
        System.err.println("Error al enviar solicitud HTTP: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

    @Override
    public List<Company> findAll() {
        // Endpoint para obtener todas las empresas
        String url = BASE_URL + "/all";

        // Realizar la petición HTTP GET
        String response = sendGetRequest(url);

        List<Company> companies = new ArrayList<>();

        if (response != null) {
            try {
                // Definir el tipo para la deserialización de la lista
                Type companyListType = new TypeToken<ArrayList<Company>>(){}.getType();
                companies = gson.fromJson(response, companyListType);
            } catch (Exception e) {
                System.err.println("Error al obtener lista de empresas: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return companies;
    }

public Company getCompanyByUserId(int userId) {
    // Construir la URL del endpoint
    String url = BASE_URL + "/user/" + userId;

    // Realizar la petición HTTP GET
    String response = sendGetRequest(url);

    Company company = null;

    // Validar que la respuesta no sea nula ni vacía
    if (response != null && !response.trim().isEmpty()) {
        try {
            company = gson.fromJson(response, Company.class);
        } catch (Exception e) {
            System.err.println("Error al obtener empresa por userId: " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        System.out.println("La respuesta está vacía o es nula.");
    }

    // Validar si se obtuvo la empresa correctamente
    if (company != null) {
        System.out.println("Empresa encontrada: " + company.toString());
    } else {
        System.out.println("No se encontró ninguna empresa para el userId: " + userId);
    }

    return company;
}


    /**
     * Método para realizar una solicitud HTTP GET
     */
    private String sendGetRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Comprobar si la respuesta es exitosa (código 200-299)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                System.err.println("Error HTTP: " + response.statusCode() + " - " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error en la solicitud GET: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método para realizar una solicitud HTTP POST con JSON
     */


    // Clases auxiliares para deserialización

    /**
     * Clase para deserializar la respuesta del endpoint check-nit
     */
    private static class ExistsResponse {
        private boolean exists;
    }

    /**
     * Clase para deserializar la respuesta del endpoint register
     */
    private static class SaveResponse {
        private String message;
        private int companyId;
    }

    /**
     * DTO para enviar al registrar una empresa
     */
    private static class CompanyDTO {
        private final String nit;
        private final String name;
        private final String sector;
        private final String contactPhone;
        private final String contactFirstName;
        private final String contactLastName;
        private final String contactPosition;

        public CompanyDTO(String nit, String name, String sector, String contactPhone, 
                         String contactFirstName, String contactLastName, String contactPosition) {
            this.nit = nit;
            this.name = name;
            this.sector = sector;
            this.contactPhone = contactPhone;
            this.contactFirstName = contactFirstName;
            this.contactLastName = contactLastName;
            this.contactPosition = contactPosition;
        }
    }
}