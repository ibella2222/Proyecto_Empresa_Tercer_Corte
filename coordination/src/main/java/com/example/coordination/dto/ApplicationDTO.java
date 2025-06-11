package com.example.coordination.dto;


/**
 * DTO que representa una postulación de un estudiante a un proyecto.
 * Se utiliza para enviar la lista de postulaciones pendientes a la GUI del coordinador.
 */
public class ApplicationDTO {

    // Datos del Estudiante
    private String studentId;
    private String studentFullName;
    private String studentProgram;

    // Datos del Proyecto
    private String projectId;
    private String projectName;
    private String projectDescription;

    // Constructor vacío
    public ApplicationDTO() {
    }

    // Constructor completo (útil para construir el objeto en el servicio)
    public ApplicationDTO(String studentId, String studentFullName, String studentProgram, String projectId, String projectName, String projectDescription) {
        this.studentId = studentId;
        this.studentFullName = studentFullName;
        this.studentProgram = studentProgram;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }


    // Getters y Setters para todos los campos...
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentFullName() { return studentFullName; }
    public void setStudentFullName(String studentFullName) { this.studentFullName = studentFullName; }
    public String getStudentProgram() { return studentProgram; }
    public void setStudentProgram(String studentProgram) { this.studentProgram = studentProgram; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }
}