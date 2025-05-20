package com.example.student.Entities;

import com.example.student.Entities.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String objectives;

    @Column(nullable = false)
    private String description;

    private int maxMonths;

    @Column(nullable = false)
    private BigDecimal budget;

    /**
     * Fecha de creación del proyecto (equivalente a date en el monolito).
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * Fecha de finalización esperada (puede ser nula).
     */
    private Date finalizationDate;

    /**
     * NIT de la empresa asociada (equivalente a company en el monolito).
     */
    @Column(nullable = false)
    private String companyNIT;

    /**
     * Estado del proyecto: RECEIVED, REJECTED, IN_EXECUTION, CLOSED.
     */
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    /**
     * Comentarios asociados al proyecto.
     */
    @ElementCollection
    private List<String> comments = new ArrayList<>();

    /**
     * Estudiantes asignados (solo como ejemplo, ajustar si necesitas relación real).
     */
    @Transient
    private List<String> students = new ArrayList<>();

    /**
     * Tareas asignadas (solo como ejemplo, ajustar si necesitas relación real).
     */
    @Transient
    private List<String> tasks = new ArrayList<>();

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

    public Date getFinalizationDate() {
        return finalizationDate;
    }

    public void setFinalizationDate(Date finalizationDate) {
        this.finalizationDate = finalizationDate;
    }

    public String getCompanyNIT() {
        return companyNIT;
    }

    public void setCompanyNIT(String companyNIT) {
        this.companyNIT = companyNIT;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
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
}