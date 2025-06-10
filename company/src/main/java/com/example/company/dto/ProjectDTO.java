package com.example.company.dto; // O el paquete DTO de tu microservicio

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object para la entidad Project.
 * Este objeto se usa para la comunicación entre microservicios (REST, RabbitMQ).
 * No contiene lógica de base de datos.
 */
public class ProjectDTO implements Serializable {

    private static final long serialVersionUID = 1L; // Buena práctica para Serializable

    private UUID id;
    private String name;
    private String summary;
    private String objectives;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate finalizationDate;

    private String state;
    private String companyNIT;
    private String justification;
    private float budget;
    private int maxMonths;

    // Constructor sin argumentos (requerido por Jackson)
    public ProjectDTO() {
    }

    // Constructor con todos los argumentos (opcional, pero útil)
    public ProjectDTO(UUID id, String name, String summary, String objectives, String description, LocalDate date, LocalDate finalizationDate, String state, String companyNIT, String justification, float budget, int maxMonths) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.objectives = objectives;
        this.description = description;
        this.date = date;
        this.finalizationDate = finalizationDate;
        this.state = state;
        this.companyNIT = companyNIT;
        this.justification = justification;
        this.budget = budget;
        this.maxMonths = maxMonths;
    }

    // --- Getters y Setters ---

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
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

    public LocalDate getFinalizationDate() {
        return finalizationDate;
    }

    public void setFinalizationDate(LocalDate finalizationDate) {
        this.finalizationDate = finalizationDate;
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

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
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
}