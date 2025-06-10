package co.edu.unicauca.proyectocurso.access;



// Clase Singleton para almacenar el token JWT durante la sesión de la aplicación.
public class AuthTokenManager {
    private static AuthTokenManager instance;
    private String jwtToken;

    private AuthTokenManager() {}

    public static AuthTokenManager getInstance() {
        if (instance == null) {
            instance = new AuthTokenManager();
        }
        return instance;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}