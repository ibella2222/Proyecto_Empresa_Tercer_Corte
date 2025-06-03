package com.example.coordination.state;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.coordination.adapter.repository.ProjectStatsRepository;
import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.domain.model.ProjectStats;

public class ReceivedState implements ProjectState {

    private final ProjectStatsRepository projectStatsRepository;

    public ReceivedState (ProjectStatsRepository projectStatsRepository){
        this.projectStatsRepository = projectStatsRepository;
    }

    @Override
    public void accept(Project project) {
        project.setState(ProjectStateEnum.ACCEPTED);
        System.out.println("✅ Proyecto aprobado. Transición a estado: ACCEPTED");

        ProjectStats evento = new ProjectStats();
        evento.setProjectId(project.getId());
        evento.setState(project.getState());
        evento.setChangeDate(LocalDate.now());
        projectStatsRepository.save(evento);
    }

    @Override
    public void reject(Project project) {
        project.setState(ProjectStateEnum.REJECTED);
        System.out.println("❌ Proyecto rechazado. Transición a estado: REJECTED");

        ProjectStats evento = new ProjectStats();
        evento.setProjectId(project.getId());
        evento.setState(project.getState());
        evento.setChangeDate(LocalDate.now());
        projectStatsRepository.save(evento);
    }

    @Override
    public void startExecution(Project project) {
        throw new IllegalStateException("⛔ No se puede iniciar la ejecución desde el estado RECEIVED.");
    }
}