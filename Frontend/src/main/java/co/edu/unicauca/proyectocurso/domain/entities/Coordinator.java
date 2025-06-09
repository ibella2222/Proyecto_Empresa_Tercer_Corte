package co.edu.unicauca.proyectocurso.domain.entities;

import java.util.List;

public class Coordinator {

    private String username;     // Identificador principal (de Keycloak)
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public Coordinator(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Coordinator() {
        // Constructor vacío para frameworks
    }

    public List<Project> evaluateProjects(List<Project> projects) {
        // Lógica de evaluación
        return projects;
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
