package com.example.coordination.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;



public class ProjectEvent {
    private UUID id;
    private String name;
    private String summary;
    private String objectives;
    private String description;
    private int maxMonths;
    private BigDecimal budget;
    private LocalDate startDate;
    private String companyNIT;
    private String status;
    
  
    public ProjectEvent(UUID id, String name, String summary, String objectives, String description, int maxMonths,
            BigDecimal budget, LocalDate startDate, String companyNIT, String status) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.objectives = objectives;
        this.description = description;
        this.maxMonths = maxMonths;
        this.budget = budget;
        this.startDate = startDate;
        this.companyNIT = companyNIT;
        this.status = status;
    }
  
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
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public String getCompanyNIT() {
        return companyNIT;
    }
    public void setCompanyNIT(String companyNIT) {
        this.companyNIT = companyNIT;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
