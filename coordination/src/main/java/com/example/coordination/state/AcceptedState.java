package com.example.coordination.state;


import com.example.coordination.dto.StudentDTO;
import com.example.coordination.entity.Project;
import com.example.coordination.entity.ProjectStateEnum;
import com.example.coordination.service.StudentService;

import java.util.List;

public class AcceptedState implements ProjectState {

    private final StudentService studentService;

    public AcceptedState(StudentService studentService) {
        this.studentService = studentService;
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
        }
    }
}