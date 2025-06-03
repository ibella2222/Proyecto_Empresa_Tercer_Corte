package com.example.coordination.domain.service;

import org.springframework.stereotype.Service;

import com.example.coordination.adapter.repository.ProjectStatsRepository;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.state.*;

@Service
public class ProjectStateFactory {

    private final StudentService studentService;
    private final ProjectStatsRepository projectStatsRepository;

    public ProjectStateFactory(StudentService studentService, ProjectStatsRepository projectStatsRepository) {
        this.studentService = studentService;
        this.projectStatsRepository = projectStatsRepository;
    }

    public ProjectState getState(ProjectStateEnum stateEnum) {
        return switch (stateEnum) {
            case RECEIVED -> new ReceivedState(projectStatsRepository);
            case ACCEPTED -> new AcceptedState(studentService, projectStatsRepository);
            case IN_EXECUTION -> new InExecutionState();
            case REJECTED -> new RejectedState();
            default -> throw new IllegalStateException("Estado no soportado: " + stateEnum);
        };
    }

    
}
