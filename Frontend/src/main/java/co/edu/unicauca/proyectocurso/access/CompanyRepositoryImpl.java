package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Company;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CompanyRepositoryImpl implements ICompanyRepository {

    private static final String BASE_URL;

    static {
        String url = "http://localhost:8083/companies";
        try {
            Properties props = new Properties();
            props.load(CompanyRepositoryImpl.class.getClassLoader().getResourceAsStream("config.properties"));
            String configUrl = props.getProperty("login.api");
            if (configUrl != null && !configUrl.trim().isEmpty()) {
                url = configUrl.trim();
            }
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar 'config.properties'. Usando URL por defecto.");
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
    public boolean save(Company company, String username) {
        try {
            String url = BASE_URL + "/register/" + username;  // ← Ahora usa username

            CompanyDTO dto = new CompanyDTO(
                    company.getNit(),
                    company.getName(),
                    company.getSector(),
                    company.getContactPhone(),
                    company.getContactFirstName(),
                    company.getContactLastName(),
                    company.getContactPosition()
            );

            String json = gson.toJson(dto);
            String response = sendPostRequestJson(url, json);

            if (response != null) {
                SaveResponse saveResponse = gson.fromJson(response, SaveResponse.class);
                return saveResponse.message != null && saveResponse.message.contains("registrada");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Company> findAll() {
        String url = BASE_URL + "/all";
        String response = sendGetRequest(url);
        List<Company> companies = new ArrayList<>();

        if (response != null) {
            try {
                Type listType = new TypeToken<List<Company>>() {}.getType();
                companies = gson.fromJson(response, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return companies;
    }

    @Override
    public Company findByNIT(String nit) {
        String url = BASE_URL + "/nit/" + nit;
        String response = sendGetRequest(url);

        if (response != null && !response.isBlank()) {
            try {
                return gson.fromJson(response, Company.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean existsCompanyNIT(String nit) {
        String url = BASE_URL + "/check-nit/" + nit;
        String response = sendGetRequest(url);

        if (response != null) {
            ExistsResponse exists = gson.fromJson(response, ExistsResponse.class);
            return exists.exists;
        }

        return false;
    }

    private String sendGetRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                System.err.println("❌ Error GET: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String sendPostRequestJson(String url, String jsonData) {
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    (code >= 200 && code < 300) ?
                            connection.getInputStream() :
                            connection.getErrorStream(),
                    StandardCharsets.UTF_8
            ));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line.trim());
            }

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // DTO para POST
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

    private static class SaveResponse {
        private String message;
    }

    private static class ExistsResponse {
        private boolean exists;
    }
}
