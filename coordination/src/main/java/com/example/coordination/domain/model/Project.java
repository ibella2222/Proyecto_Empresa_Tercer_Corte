package com.example.coordination.domain.model;
import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;



@Entity
@Table(name = "projects")
public class Project {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String companyNit;

    @Enumerated(EnumType.STRING)
    private ProjectStateEnum state;

    @Column(nullable = true)
    private String summary;

    @Column(nullable = true)
    private String objectives;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private int maxMonths;

    @Column(nullable = true)
    private BigDecimal budget;

    @Column(nullable = true)
    private LocalDate date;

    public Project() {}

    public Project(UUID id, String name, String companyNit, String summary, String objectives, String description, 
                   int maxMonths, BigDecimal budget, LocalDate date, ProjectStateEnum state) {
        this.id = id;
        this.name = name;
        this.companyNit = companyNit;
        this.summary = summary;
        this.objectives = objectives;
        this.description = description;
        this.maxMonths = maxMonths;
        this.budget = budget;
        this.date = date;
        this.state = state;
    }

    // Getters y Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyNit() {
        return companyNit;
    }

    public void setCompanyNit(String companyNit) {
        this.companyNit = companyNit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStateEnum getState() {
        return state;
    }

    public void setState(ProjectStateEnum state) {
        this.state = state;
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

    public int getMaxMonths() {
        return maxMonths;
    }

    public void setMaxMonths(int maxMonths) {
        this.maxMonths = maxMonths;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}