package com.example.coordination.state;


import com.example.coordination.adapter.repository.ProjectStatsRepository;
import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.domain.model.ProjectStats;
import com.example.coordination.domain.service.StudentService;
import com.example.coordination.dto.StudentDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AcceptedState implements ProjectState {

    private final StudentService studentService;
    private final ProjectStatsRepository projectStatsRepository;


    public AcceptedState(StudentService studentService, ProjectStatsRepository projectStatsRepository) {
        this.studentService = studentService;
        this.projectStatsRepository = projectStatsRepository;
    }

    @Override
    public void accept(Project project) {
        throw new IllegalStateException("El proyecto ya está en estado ACCEPTED.");
    }

    @Override
    public void reject(Project project) {
        throw new IllegalStateException("No se puede rechazar un proyecto ya aceptado.");
    }

   @Override
    public void startExecution(Project project) {
        List<StudentDTO> estudiantes = studentService.getStudentsByProjectId(project.getId().toString());

        if (estudiantes.size() == 1) {
            project.setState(ProjectStateEnum.IN_EXECUTION);
            ProjectStats evento = new ProjectStats();
            evento.setProjectId(project.getId());
            evento.setState(project.getState());
            evento.setChangeDate(LocalDate.now());
            projectStatsRepository.save(evento);
        }
    }
}