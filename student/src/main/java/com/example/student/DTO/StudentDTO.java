package com.example.student.DTO;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String program;
    private String projectId;
    
    // Default constructor needed for serialization
    public StudentDTO() {
    }
    
    public StudentDTO(int id, String username, String firstName, String lastName, String program, String projectId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.program = program;
        this.projectId = projectId;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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
    
    public String getProgram() {
        return program;
    }
    
    public void setProgram(String program) {
        this.program = program;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", program='" + program + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}