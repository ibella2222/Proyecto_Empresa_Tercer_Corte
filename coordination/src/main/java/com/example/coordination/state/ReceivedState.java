package com.example.coordination.state;

import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;

public class ReceivedState implements ProjectState {

    @Override
    public void accept(Project project) {
        project.setState(ProjectStateEnum.ACCEPTED);
        System.out.println("✅ Proyecto aprobado. Transición a estado: ACCEPTED");
    }

    @Override
    public void reject(Project project) {
        project.setState(ProjectStateEnum.REJECTED);
        System.out.println("❌ Proyecto rechazado. Transición a estado: REJECTED");
    }

    @Override
    public void startExecution(Project project) {
        throw new IllegalStateException("⛔ No se puede iniciar la ejecución desde el estado RECEIVED.");
    }
}