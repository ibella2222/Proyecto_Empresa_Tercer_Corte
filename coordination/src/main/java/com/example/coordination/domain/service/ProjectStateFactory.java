package com.example.coordination.domain.service;

import org.springframework.stereotype.Service;

import com.example.coordination.state.ProjectState;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.state.*;

@Service
public class ProjectStateFactory {

    private final StudentService studentService;

    public ProjectStateFactory(StudentService studentService) {
        this.studentService = studentService;
    }

    public ProjectState getState(ProjectStateEnum stateEnum) {
        return switch (stateEnum) {
            case RECEIVED -> new ReceivedState();
            case ACCEPTED -> new AcceptedState(studentService);
            case IN_EXECUTION -> new InExecutionState();
            case REJECTED -> new RejectedState();
            default -> throw new IllegalStateException("Estado no soportado: " + stateEnum);
        };
    }

    
}
