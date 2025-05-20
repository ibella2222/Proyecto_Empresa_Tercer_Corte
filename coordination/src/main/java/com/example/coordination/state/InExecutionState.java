package com.example.coordination.state;

import com.example.coordination.entity.Project;


public class InExecutionState implements ProjectState {

    @Override
    public void accept(Project project) {
        throw new IllegalStateException("No se puede aceptar un proyecto que ya está en ejecución.");
    }

    @Override
    public void reject(Project project) {
        throw new IllegalStateException("No se puede rechazar un proyecto que está en ejecución.");
    }

    @Override
    public void startExecution(Project project) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startExecution'");
    }
}
