package com.example.coordination.domain.service;


import com.example.coordination.adapter.messaging.ProjectEventPublisher;
import com.example.coordination.adapter.repository.ProjectRepository;
import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.state.ProjectState;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStateFactory stateFactory;
    private final ProjectEventPublisher eventPublisher;
    

    public ProjectService(ProjectRepository projectRepository, ProjectStateFactory stateFactory, ProjectEventPublisher eventPublisher) {
        this.projectRepository = projectRepository;
        this.stateFactory = stateFactory;
        this.eventPublisher = eventPublisher;
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> getById(UUID id) {
        return projectRepository.findById(id);
    }

    public void acceptProject(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + projectId));
    
        ProjectState state = stateFactory.getState(project.getState());
        state.accept(project);
    
        project.setState(ProjectStateEnum.ACCEPTED);
        projectRepository.save(project);
    
        eventPublisher.sendProjectStateChange(
            project.getId(),
            project.getName(),
            project.getSummary(),
            project.getObjectives(),
            project.getDescription(),
            project.getMaxMonths(),
            project.getBudget(), // Asegúrate de que este cast sea seguro
            project.getStartDate(),
            project.getCompanyNit(),
            project.getState()
        );
    }


    public void rejectProject(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    
        ProjectState state = stateFactory.getState(project.getState());
        state.reject(project);
    
        Project savedProject = projectRepository.save(project);
    
        eventPublisher.sendProjectStateChange(
            savedProject.getId(),
            savedProject.getName(),
            savedProject.getSummary(),
            savedProject.getObjectives(),
            savedProject.getDescription(),
            savedProject.getMaxMonths(),
            savedProject.getBudget(),
            savedProject.getStartDate(),
            savedProject.getCompanyNit(),
            savedProject.getState()
        );
    
    }

    public void startExecution(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        ProjectState stateInstance = stateFactory.getState(project.getState());
        stateInstance.startExecution(project);
        
        Project savedProject = projectRepository.save(project);
        
        eventPublisher.sendProjectStateChange(
            savedProject.getId(),
            savedProject.getName(),
            savedProject.getSummary(),
            savedProject.getObjectives(),
            savedProject.getDescription(),
            savedProject.getMaxMonths(),
            savedProject.getBudget(),
            savedProject.getStartDate(),
            savedProject.getCompanyNit(),
            savedProject.getState()
        );
        

        projectRepository.save(project); // Persistimos el nuevo estado
    }



}