package com.example.coordination.state;

import com.example.coordination.entity.Project;


public class RejectedState implements ProjectState {

    @Override
    public void accept(Project project) {
        throw new IllegalStateException("No se puede aceptar un proyecto rechazado.");
    }

    @Override
    public void reject(Project project) {
        throw new IllegalStateException("El proyecto ya está en estado REJECTED.");
    }

    @Override
    public void startExecution(Project project) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startExecution'");
    }
}
