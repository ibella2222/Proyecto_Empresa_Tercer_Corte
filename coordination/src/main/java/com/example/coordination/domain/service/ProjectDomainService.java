package com.example.coordination.domain.service;
import com.example.coordination.domain.model.Project;
import com.example.coordination.state.ProjectState;


import org.springframework.stereotype.Service;


@Service
public class ProjectDomainService {

    private final ProjectStateFactory stateFactory;

    public ProjectDomainService(ProjectStateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    public Project accept(Project project) {
        ProjectState state = stateFactory.getState(project.getState());
        state.accept(project);
        return project;
    }

    public Project reject(Project project) {
        ProjectState state = stateFactory.getState(project.getState());
        state.reject(project);
        return project;
    }

    public Project startExecution(Project project) {
        ProjectState state = stateFactory.getState(project.getState());
        state.startExecution(project);
        return project;
    }
}