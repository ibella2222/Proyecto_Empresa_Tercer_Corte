package com.example.coordination.application.port.in;

import java.util.UUID;

public interface ProjectUseCase {
    void acceptProject(UUID id);
    void rejectProject(UUID id);
    void startExecution(UUID id);
}


