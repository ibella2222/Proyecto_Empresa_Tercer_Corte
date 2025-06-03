package co.edu.unicauca.proyectocurso.domain.entities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Lenovo pc
 */
import co.edu.unicauca.proyectocurso.domain.entities.ProjectState;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProjectStats {

    private Long id;
    private UUID projectId;
    private ProjectState state;
    private LocalDate changeDate;

    public ProjectStats() {
    }

    public ProjectStats(Long id, UUID projectId, ProjectState state, LocalDate changeDate) {
        this.id = id;
        this.projectId = projectId;
        this.state = state;
        this.changeDate = changeDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public ProjectState getState() {
        return state;
    }

    public void setState(ProjectState state) {
        this.state = state;
    }

    public LocalDate getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDate changeDate) {
        this.changeDate = changeDate;
    }
}
