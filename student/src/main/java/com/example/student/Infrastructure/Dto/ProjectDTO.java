package com.example.student.Infrastructure.Dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// ⭐ CAMBIO IMPORTANTE: Ignora cualquier otro campo desconocido que pueda llegar en el futuro
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String justification; // Asegúrate de que este campo esté
    private float budget;
    private int maxMonths;

    // ⭐ CAMBIO: Añadidos para coincidir con el JSON recibido
    private List<String> comments;
    private List<String> students;

    public ProjectDTO() {
    }

    // --- Getters y Setters para todos los campos ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getObjectives() { return objectives; }
    public void setObjectives(String objectives) { this.objectives = objectives; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDate getFinalizationDate() { return finalizationDate; }
    public void setFinalizationDate(LocalDate finalizationDate) { this.finalizationDate = finalizationDate; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCompanyNIT() { return companyNIT; }
    public void setCompanyNIT(String companyNIT) { this.companyNIT = companyNIT; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public float getBudget() { return budget; }
    public void setBudget(float budget) { this.budget = budget; }

    public int getMaxMonths() { return maxMonths; }
    public void setMaxMonths(int maxMonths) { this.maxMonths = maxMonths; }

    public List<String> getComments() { return comments; }
    public void setComments(List<String> comments) { this.comments = comments; }

    public List<String> getStudents() { return students; }
    public void setStudents(List<String> students) { this.students = students; }
}
