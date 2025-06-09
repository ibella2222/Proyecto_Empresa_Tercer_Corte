package com.example.student.Domain.Model;


public class Student {

    private String id; // Keycloak 'sub' (UUID)
    private String username;
    private String firstName;
    private String lastName;
    private String program;
    private String projectId; // UUID del proyecto

    // Constructores, Getters y Setters

    public Student(String id, String username, String firstName, String lastName, String program) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.program = program;
    }

    public Student() {}

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
