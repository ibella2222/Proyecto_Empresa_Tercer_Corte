package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class UserRepositoryImpl implements IUserRepository {

    private final HttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/api/auth";

    @Override
    public boolean save(User user) {
        try {
            // URL del endpoint de registro
            String apiUrl = baseUrl + "/register";

            // Convertir objeto a JSON
            String json = mapper.writeValueAsString(user);
            System.out.println("JSON a enviar: " + json);
            
            // Crear solicitud POST
            HttpPost request = new HttpPost(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(json));

            // Ejecutar la solicitud
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            // Retornar true si el usuario fue creado (HTTP 201)
            return statusCode == 201;
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // Método para registrar usuario
public boolean registerUser(String username, String password, String role, int id) {
    return save(new User(username, password, role, id));
}


    @Override
    public List<User> findAll() {
        try {
            // Se asume que hay un endpoint que devuelve todos los usuarios, por ejemplo /users
            String apiUrl = baseUrl + "/users";
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            
            HttpResponse response = httpClient.execute(request);
            try (InputStream content = response.getEntity().getContent()) {
                return mapper.readValue(content, new TypeReference<List<User>>() {});
            }
        } catch (Exception e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
            return List.of();
        }
    }

    // Método para validar usuario y obtener su rol
    public String getUserRole(String username, String password) {
        try {
            // Se asume que existe un endpoint que valida y retorna el rol del usuario
            String apiUrl = baseUrl + "/user/role?username=" + username + "&password=" + password;
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            
            HttpResponse response = httpClient.execute(request);
            try (InputStream content = response.getEntity().getContent()) {
                // Suponiendo que el endpoint retorna { "role": "ALGO" }
                return mapper.readTree(content).get("role").asText();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener rol del usuario: " + e.getMessage());
            return null;
        }
    }

    // Método para verificar si un usuario existe
    public boolean userExists(String username) {
        try {
            String apiUrl = baseUrl + "/user/exists?username=" + username;
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            
            HttpResponse response = httpClient.execute(request);
            // Se asume que el endpoint retorna { "exists": true } o false
            try (InputStream content = response.getEntity().getContent()) {
                return mapper.readTree(content).get("exists").asBoolean();
            }
        } catch (Exception e) {
            System.err.println("Error al verificar existencia de usuario: " + e.getMessage());
            return false;
        }
    }
// Método para obtener un usuario por username
public User getUser(String username) {
    try {
        String apiUrl = baseUrl + "/user/" + username;
        HttpGet request = new HttpGet(apiUrl);
        request.setHeader("Accept", "application/json");

        HttpResponse response = httpClient.execute(request);

        // Leer el contenido del InputStream y convertirlo en un String
        String responseString = EntityUtils.toString(response.getEntity());

        // Deserializar el String en un objeto User
        return mapper.readValue(responseString, User.class);
    } catch (Exception e) {
        System.err.println("Error al obtener usuario: " + e.getMessage());
        return null;
    }
}
    // Método para buscar usuarios filtrando por nombre y rol
    public List<User> searchUserss(String name, String role) {
        try {
            // Se asume un endpoint con parámetros de consulta para nombre y rol
            String apiUrl = baseUrl + "/users/search?name=" + name + "&role=" + (role == null ? "" : role);
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            
            HttpResponse response = httpClient.execute(request);
            try (InputStream content = response.getEntity().getContent()) {
                return mapper.readValue(content, new TypeReference<List<User>>() {});
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuarios por nombre y rol: " + e.getMessage());
            return List.of();
        }
    }
    // Método para actualizar usuario
    public boolean updateUser(String oldUsername, String newUsername, String newPassword, String newRole) {
        try {
            String apiUrl = baseUrl + "/user/update";
            // Se envían los datos en JSON
            String json = mapper.writeValueAsString(
                    new UserUpdate(oldUsername, newUsername, newPassword, newRole));
            HttpPut request = new HttpPut(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(json));
            
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // Método para buscar usuarios por query
    public List<User> searchUsers(String query) {
        try {
            String apiUrl = baseUrl + "/users/" + query;
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            
            HttpResponse response = httpClient.execute(request);
            try (InputStream content = response.getEntity().getContent()) {
                return mapper.readValue(content, new TypeReference<List<User>>() {});
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuarios: " + e.getMessage());
            return List.of();
        }
    }

    // Método para eliminar un usuario
    public boolean deleteUser(String username) {
        try {
            String apiUrl = baseUrl + "/user/" + username;
            HttpDelete request = new HttpDelete(apiUrl);
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200 || statusCode == 204;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // Método para verificar si el perfil del usuario está completado
    public boolean isProfileCompleted(String username) {
        try {
            String apiUrl = baseUrl + "/user/" + username + "/profileCompleted";
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            
            HttpResponse response = httpClient.execute(request);
            try (InputStream content = response.getEntity().getContent()) {
                return mapper.readTree(content).get("profileCompleted").asBoolean();
            }
        } catch (Exception e) {
            System.err.println("Error al verificar perfil completado: " + e.getMessage());
            return false;
        }
    }

    // Método para actualizar el estado de profileCompleted del usuario
    public boolean updateProfileCompleted(String username, boolean completed) {
        try {
            String apiUrl = baseUrl + "/user/" + username + "/profileCompleted";
            // Se envía el nuevo estado en JSON
            String json = mapper.writeValueAsString(new ProfileCompletedUpdate(completed));
            HttpPut request = new HttpPut(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(json));
            
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (Exception e) {
            System.err.println("Error al actualizar profileCompleted: " + e.getMessage());
            return false;
        }
    }

    // Clases auxiliares para las actualizaciones (ajusta los campos según tus necesidades)
    private static class UserUpdate {
        public String oldUsername;
        public String newUsername;
        public String newPassword;
        public String newRole;
        
        public UserUpdate(String oldUsername, String newUsername, String newPassword, String newRole) {
            this.oldUsername = oldUsername;
            this.newUsername = newUsername;
            this.newPassword = newPassword;
            this.newRole = newRole;
        }
    }
    
    private static class ProfileCompletedUpdate {
        public boolean profileCompleted;
        
        public ProfileCompletedUpdate(boolean profileCompleted) {
            this.profileCompleted = profileCompleted;
        }
    }
}
