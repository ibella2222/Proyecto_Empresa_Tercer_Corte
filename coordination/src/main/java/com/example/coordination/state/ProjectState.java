package com.example.coordination.state;

import com.example.coordination.domain.model.Project;

public interface ProjectState {
    void accept(Project project);
    void reject(Project project);
    void startExecution(Project project);
}
