package co.edu.unicauca.proyectocurso.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private String username; // Username que viene desde Keycloak (ej: email)
    private String nit;
    private String name;
    private String sector;
    private String contactPhone;
    private String contactFirstName;
    private String contactLastName;
    private String contactPosition;
    private List<Project> projects;
    public boolean isProfileComplete;


    public Company() {
        this.projects = new ArrayList<>();
    }

    public Company(String username, String nit, String name, String sector,
                   String contactPhone, String contactFirstName, String contactLastName,
                   String contactPosition) {
        this.username = username;
        this.nit = nit;
        this.name = name;
        this.sector = sector;
        this.contactPhone = contactPhone;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactPosition = contactPosition;
        this.projects = new ArrayList<>();
    }

    // Métodos
    public void addProject(Project project) {
        projects.add(project);
    }

    // Getters y setters
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getNit() { return nit; }

    public void setNit(String nit) { this.nit = nit; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSector() { return sector; }

    public void setSector(String sector) { this.sector = sector; }

    public String getContactPhone() { return contactPhone; }

    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactFirstName() { return contactFirstName; }

    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }

    public String getContactLastName() { return contactLastName; }

    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }

    public String getContactPosition() { return contactPosition; }

    public void setContactPosition(String contactPosition) { this.contactPosition = contactPosition; }

    public List<Project> getProjects() { return projects; }

    public void setProjects(List<Project> projects) { this.projects = projects; }

    @Override
    public String toString() {
        return "Company{" +
                "username='" + username + '\'' +
                ", nit='" + nit + '\'' +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                '}';
    }
}
