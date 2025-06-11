package co.edu.unicauca.proyectocurso.domain.entities; // O el paquete que prefieras

public class ApplicationDTO {
    private String studentId;
    private String studentFullName;
    private String studentProgram;
    private String projectId;
    private String projectName;
    private String projectDescription;

    // Getters y Setters para todos los campos...
    public String getStudentId() { return studentId; }
    public String getStudentFullName() { return studentFullName; }
    public String getStudentProgram() { return studentProgram; }
    public String getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getProjectDescription() { return projectDescription; }
}