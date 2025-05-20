package com.example.student.DTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO que representa la estructura de Project esperada por el frontend
 */
public class ProjectDTO {
    private UUID id;
    private String name;
    private String description;
    private Date FinalizationDate;
    private LocalDate date;
    private String state; // En lugar de usar enum, usamos String para mayor flexibilidad
    private String companyNIT; // En lugar de Company object, usamos su NIT
    private List<String> comments;
    private List<String> students; // IDs o nombres de estudiantes
    private List<String> tasks; // IDs o nombres de tareas
    private float budget;
    private int maxMonths;
    private String objectives;
    private String summary;

    // Constructor vacío
    public ProjectDTO() {
        this.id = UUID.randomUUID();
        this.name = "Proyecto sin nombre";
        this.description = "Descripción no definida";
        this.date = LocalDate.now();
        this.state = "RECEIVED";
        this.comments = new ArrayList<>();
        this.students = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Constructor con parámetros esenciales
    public ProjectDTO(String name, String summary, String objectives, String description,
                      int maxMonths, float budget, LocalDate date, Date finalizationDate) {
        this.id = UUID.randomUUID();
        this.name = (name != null) ? name : "Proyecto sin nombre";
        this.summary = summary;
        this.description = (description != null) ? description : "Descripción no definida";
        this.date = date != null ? date : LocalDate.now();
        this.FinalizationDate = finalizationDate;
        this.state = "RECEIVED";
        this.comments = new ArrayList<>();
        this.students = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.budget = budget;
        this.maxMonths = maxMonths;
        this.objectives = (objectives != null) ? objectives : "";
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompanyNIT() {
        return companyNIT;
    }

    public void setCompanyNIT(String companyNIT) {
        this.companyNIT = companyNIT;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public Date getFinalizationDate() {
        return FinalizationDate;
    }

    public void setFinalizationDate(Date FinalizationDate) {
        this.FinalizationDate = FinalizationDate;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public int getMaxMonths() {
        return maxMonths;
    }

    public void setMaxMonths(int maxMonths) {
        this.maxMonths = maxMonths;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}