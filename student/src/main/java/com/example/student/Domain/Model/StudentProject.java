package com.example.student.Domain.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "available_projects")
@Data
public class StudentProject implements Serializable {

    @Id
    private UUID id; // Usamos el ID que viene del DTO

    private String name;
    private String summary;
    private String objectives;
    private String description;
    private LocalDate date;
    private LocalDate finalizationDate;
    private String state; // Estado local, ej: "AVAILABLE"
    private String companyNIT;
    private String justification;
    private float budget;
    private int maxMonths;

    // No guardaremos los campos 'comments' ni 'students' en la base de datos por ahora,
    // a menos que la lógica de negocio lo requiera. El DTO los recibe, pero aquí los ignoramos.

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
