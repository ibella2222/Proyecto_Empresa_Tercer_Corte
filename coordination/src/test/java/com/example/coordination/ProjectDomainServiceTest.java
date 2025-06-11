package com.example.coordination;

import com.example.coordination.domain.model.Project;
import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.domain.service.ProjectDomainService;
import com.example.coordination.domain.service.ProjectStateFactory;
import com.example.coordination.state.ProjectState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProjectDomainServiceTest {

    private ProjectStateFactory stateFactory;
    private ProjectDomainService domainService;
    private ProjectState mockState;

    @BeforeEach
    void setUp() {
        // Mockeamos la fábrica y el estado
        stateFactory = mock(ProjectStateFactory.class);
        mockState = mock(ProjectState.class);
        // Inyectamos la fábrica al servicio
        domainService = new ProjectDomainService(stateFactory);
    }

    @Test
    void testStartExecutionChangesState() {
        // Arrange: Creamos un proyecto en estado ACCEPTED
        Project project = new Project(
                UUID.randomUUID(),
                "Proyecto Prueba",
                "123456789",
                "Resumen",
                "Objetivos",
                "Descripción",
                5,
                new BigDecimal("8000.00"),
                LocalDate.now(),
                ProjectStateEnum.ACCEPTED
        );

        // Cuando se llame a getState con ACCEPTED, devolver el mock del estado
        when(stateFactory.getState(ProjectStateEnum.ACCEPTED)).thenReturn(mockState);

        // Act: Ejecutamos el cambio de estado
        Project result = domainService.startExecution(project);

        // Assert: Verificamos que se llamó al método correcto del estado mockeado
        verify(mockState).startExecution(project);
        assertEquals(project, result);
    }
}
