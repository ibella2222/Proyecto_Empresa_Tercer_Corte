package com.example.coordination.application.port.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.coordination.adapter.messaging.ProjectEventPublisher;
import com.example.coordination.adapter.repository.ProjectRepository;
import com.example.coordination.application.port.in.ProjectUseCase;
import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.service.ProjectDomainService;

@Service
public class ProjectApplicationService implements ProjectUseCase {

    private final ProjectRepository projectRepository;
    private final ProjectEventPublisher eventPublisher;
    private final ProjectDomainService domainService;

    public ProjectApplicationService(ProjectRepository projectRepository,
                                     ProjectEventPublisher eventPublisher,
                                     ProjectDomainService domainService) {
        this.projectRepository = projectRepository;
        this.eventPublisher = eventPublisher;
        this.domainService = domainService;
    }

    @Override
    public void acceptProject(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        domainService.accept(project);
        projectRepository.save(project);
        publishEvent(project);
    }

    @Override
    public void rejectProject(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        domainService.reject(project);
        projectRepository.save(project);
        publishEvent(project);
    }

    @Override
    public void startExecution(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        domainService.startExecution(project);
        projectRepository.save(project);
        publishEvent(project);
    }

    private void publishEvent(Project project) {
        eventPublisher.sendProjectStateChange(
            project.getId(),
            project.getName(),
            project.getSummary(),
            project.getObjectives(),
            project.getDescription(),
            project.getMaxMonths(),
            project.getBudget(),
            project.getDate(),
            project.getCompanyNit(),
            project.getState()
        );
    }
}