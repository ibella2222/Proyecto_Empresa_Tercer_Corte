package com.example.coordination.application.port.service;

import com.example.coordination.application.port.in.ApplicationUseCase;
import com.example.coordination.dto.ApplicationDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del caso de uso para gestionar las postulaciones.
 * La anotación @Service le dice a Spring que esta es la clase que debe
 * usar cuando se necesite un bean de tipo ApplicationUseCase.
 */
@Service
public class ApplicationService implements ApplicationUseCase {

    // En un futuro, aquí inyectarías los puertos de salida (repositorios)
    // para interactuar con la base de datos.
    // private final ApplicationRepositoryPort applicationRepository;
    // public ApplicationService(ApplicationRepositoryPort applicationRepository) { ... }

    @Override
    public List<ApplicationDTO> getPendingApplications() {
        // TODO: Implementar la lógica real para buscar en la base de datos
        // del coordinador las postulaciones pendientes.

        System.out.println("DEBUG: Se llamó a getPendingApplications. Devolviendo lista de prueba.");

        // Por ahora, devolvemos una lista de prueba para que puedas probar la GUI.
        List<ApplicationDTO> dummyList = new ArrayList<>();
        dummyList.add(new ApplicationDTO("student-id-123", "Juan Pérez", "Sistemas", "project-id-abc", "Proyecto Alpha", "Descripción del proyecto Alpha."));
        dummyList.add(new ApplicationDTO("student-id-456", "Ana Gómez", "Electrónica", "project-id-def", "Proyecto Beta", "Descripción del proyecto Beta."));
        return dummyList;
    }

    @Override
    public void approveApplication(String studentId, String projectId) {
        // TODO: Implementar la lógica real:
        // 1. Buscar la postulación en la base de datos.
        // 2. Cambiar su estado a "APPROVED".
        // 3. Posiblemente, enviar un mensaje a RabbitMQ para notificar a otros servicios.

        System.out.println(String.format("DEBUG: Se aprobó la postulación para el estudiante %s en el proyecto %s.", studentId, projectId));
    }

    @Override
    public void rejectApplication(String studentId, String projectId) {
        // TODO: Implementar la lógica real:
        // 1. Buscar la postulación en la base de datos.
        // 2. Cambiar su estado a "REJECTED" o eliminarla.

        System.out.println(String.format("DEBUG: Se rechazó la postulación para el estudiante %s en el proyecto %s.", studentId, projectId));
    }
}
