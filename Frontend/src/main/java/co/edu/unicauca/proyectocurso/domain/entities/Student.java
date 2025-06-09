package co.edu.unicauca.proyectocurso.domain.entities;

public class Student {

    private String username;     // Identificador funcional desde Keycloak (email, username, etc.)
    private String firstName;
    private String lastName;
    private String program;
    private String projectId;

    // Constructor completo
    public Student(String username, String firstName, String lastName, String program, String projectId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.program = program;
        this.projectId = projectId;
    }

    // Constructor vacío
    public Student() {
    }

    // Getters y setters
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getRole() {
        return "Student";
    }
}
