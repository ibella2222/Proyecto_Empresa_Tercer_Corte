package com.example.student.Infrastructure.Dto;



import java.io.Serializable;

// DTO para exponer la información del estudiante
public class StudentDTO implements Serializable {

    private String id; // El 'sub' de Keycloak
    private String username;
    private String firstName;
    private String lastName;
    private String program;
    private String projectId;

    // Constructor vacío
    public StudentDTO() {
    }

    // Constructor con todos los parámetros
    public StudentDTO(String id, String username, String firstName, String lastName, String program, String projectId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.program = program;
        this.projectId = projectId;
    }

    // Getters y Setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
}