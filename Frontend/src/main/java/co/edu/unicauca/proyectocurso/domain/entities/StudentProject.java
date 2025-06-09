package co.edu.unicauca.proyectocurso.domain.entities;

public class StudentProject {

    private int id;
    private String studentUsername;
    private String projectId;
    private Student student;
    private Project project;
    private Status status = Status.RECEIVED;

    // Constructor completo
    public StudentProject(int id, String studentUsername, String projectId, Status status) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.projectId = projectId;
        this.student = new Student(); // Puedes asociar luego si lo necesitas
        this.project = new Project(); // Igual aquí
        this.status = status;
    }

    // Constructor vacío
    public StudentProject() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Enum para el estado del proyecto
    public enum Status {
        RECEIVED, ACCEPTED, REJECTED, IN_EXECUTION, CLOSED
    }

    @Override
    public String toString() {
        return "StudentProject{" +
                "id=" + id +
                ", studentUsername='" + studentUsername + '\'' +
                ", projectId='" + projectId + '\'' +
                ", status=" + status +
                '}';
    }
}
