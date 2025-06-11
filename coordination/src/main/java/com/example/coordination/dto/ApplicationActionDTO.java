package com.example.coordination.dto;

/**
 * DTO utilizado para transportar los IDs necesarios cuando un coordinador
 * realiza una acción (aprobar, rechazar) sobre una postulación.
 */
public class ApplicationActionDTO {

    private String studentId;
    private String projectId;

    // Constructor vacío (necesario para la deserialización de JSON)
    public ApplicationActionDTO() {
    }

    // Getters y Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}