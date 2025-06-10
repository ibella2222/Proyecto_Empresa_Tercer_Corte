package com.example.student.Infrastructure.Dto;

// El DTO ahora solo pide la información que no podemos obtener del token.
public class UpdateProfileRequestDTO {

    private String program; // El único campo que el usuario debe proporcionar

    // Getters y Setters
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
