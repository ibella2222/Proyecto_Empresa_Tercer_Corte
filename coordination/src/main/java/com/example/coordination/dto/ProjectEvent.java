package com.example.coordination.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

@NoArgsConstructor 
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectEvent implements Serializable {
    private UUID id;
    private String name;
    private String summary;
    private String objectives;
    private String description;
    private int maxMonths;
    private BigDecimal budget;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    private String companyNIT;
    private String state;
     
  
    public ProjectEvent(UUID id, String name, String summary, String objectives, String description, int maxMonths,
            BigDecimal budget, LocalDate date, String companyNIT, String state) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.objectives = objectives;
        this.description = description;
        this.maxMonths = maxMonths;
        this.budget = budget;
        this.date = date;
        this.companyNIT = companyNIT;
        this.state = state;
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
    public LocalDate getDate() {
        return date;
    }
    public void setStartDate(LocalDate date) {
        this.date = date;
    }
    public String getCompanyNIT() {
        return companyNIT;
    }
    public void setCompanyNIT(String companyNIT) {
        this.companyNIT = companyNIT;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
