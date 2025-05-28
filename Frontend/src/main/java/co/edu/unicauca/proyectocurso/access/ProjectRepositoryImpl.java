package co.edu.unicauca.proyectocurso.access;
import co.edu.unicauca.proyectocurso.adapterDate.LocalDateAdapter;
import co.edu.unicauca.proyectocurso.domain.entities.Company;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import co.edu.unicauca.proyectocurso.domain.entities.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


/**
 * Implementación del repositorio de proyectos usando llamadas HTTP al microservicio Company.
 */
public class ProjectRepositoryImpl implements IProjectRepository {

    private static final String BASE_URL;
    private final ICompanyRepository companyRepository = new CompanyRepositoryImpl();

    static {
        String url = "http://localhost:8083/projects"; // Valor por defecto
        try {
            Properties props = new Properties();
            props.load(ProjectRepositoryImpl.class.getClassLoader().getResourceAsStream("config.properties"));
            String configUrl = props.getProperty("company.api");
            if (configUrl != null && !configUrl.trim().isEmpty()) {
                url = configUrl.trim().replace("/api/companies", "/api/projects");
            }
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar 'config.properties'. Usando URL por defecto.");
        }
        BASE_URL = url;
    }

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public boolean save(Project project, String nitEmpresa) {
        
        try {
            project.setCompanyNIT(nitEmpresa); // ✅ Setea el NIT dentro del objeto antes de enviarlo
            String jsonData = gson.toJson(project);
            System.out.println("➡ JSON enviado al microservicio:");
            System.out.println(jsonData);
            String response = sendPostRequest(BASE_URL, jsonData);
            return response != null && response.contains("registrado");
        } catch (Exception e) {
            System.err.println("Error al guardar proyecto: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<Project> findAll() {
        try {
            String response = sendGetRequest(BASE_URL + "/all");
            Type listType = new TypeToken<ArrayList<Project>>() {}.getType();
            List<Project> projects = gson.fromJson(response, listType);

            // Asociar la empresa completa a cada proyecto usando companyNIT
            for (Project project : projects) {
                String nit = project.getCompanyNIT();
                if (nit != null && !nit.isEmpty()) {
                    Company fullCompany = companyRepository.findByNIT(nit);
                    if (fullCompany != null) {
                        project.setCompany(fullCompany);
                    } else {
                        System.err.println("⚠️ Empresa con NIT " + nit + " no encontrada.");
                    }
                } else {
                    System.err.println("⚠️ Proyecto sin NIT de empresa: " + project.getName());
                }
            }

            return projects;
        } catch (Exception e) {
            System.err.println("Error al obtener proyectos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    @Override
    public boolean update(Project project) {
        try {
            String jsonData = gson.toJson(project);
            String response = sendPostRequest(BASE_URL + "/update", jsonData);
            return response != null && response.contains("actualizado");
        } catch (Exception e) {
            System.err.println("Error al actualizar proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(UUID projectId) {
        try {
            String response = sendPostRequest(BASE_URL + "/delete/" + projectId, "");
            return response != null && response.contains("eliminado");
        } catch (Exception e) {
            System.err.println("Error al eliminar proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Project> findProjectsByCompanyNIT(String nit) {
        try {
            String response = sendGetRequest(BASE_URL + "/company/" + nit);
            Type listType = new TypeToken<ArrayList<Project>>() {}.getType();
            return gson.fromJson(response, listType);
        } catch (Exception e) {
            System.err.println("Error al obtener proyectos por NIT: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Project findById(UUID id) {
        try {
            String response = sendGetRequest(BASE_URL + "/" + id);
            return gson.fromJson(response, Project.class);
        } catch (Exception e) {
            System.err.println("Error al buscar proyecto por ID: " + e.getMessage());
            return null;
        }
    }

    private String sendGetRequest(String url) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        if (con.getResponseCode() >= 200 && con.getResponseCode() < 300) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line.trim());
            return response.toString();
        } else {
            System.err.println("GET " + url + " => " + con.getResponseCode());
            return null;
        }
    }

    private String sendPostRequest(String url, String jsonData) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonData.getBytes(StandardCharsets.UTF_8));
        }

        if (con.getResponseCode() >= 200 && con.getResponseCode() < 300) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line.trim());
            return response.toString();
        } else {
            System.err.println("POST " + url + " => " + con.getResponseCode());
            return null;
        }
    }
}
