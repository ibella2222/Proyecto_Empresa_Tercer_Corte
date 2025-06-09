package com.example.student.Infrastructure.Dto;

// DTO para la solicitud de actualización de perfil
public class UpdateProfileRequestDTO {
    private String firstName;
    private String lastName;
    private String program;

    // Getters y Setters...
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
}
