package com.example.student.Listener;

import com.example.student.Infrastructure.Dto.ProjectDTO;
import com.example.student.Domain.Service.StudentProjectService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectEventListener {

    private final StudentProjectService studentProjectService;

    @Autowired
    public ProjectEventListener(StudentProjectService studentProjectService) {
        this.studentProjectService = studentProjectService;
    }

    /**
     * Listener en modo normal.
     * Espera un objeto ProjectDTO, que Spring convertirá automáticamente.
     */
    @RabbitListener(queues = "projects.queue.for-student-service")
    public void handleNewProjectMessage(ProjectDTO project) { // <-- Restaurado a ProjectDTO
        System.out.println("\n\n--- [DEBUG 1: LISTENER] ---");
        System.out.println("Mensaje recibido y CONVERTIDO A DTO exitosamente.");
        System.out.println("DATOS RECIBIDOS (DTO): " + project.toString());

        try {
            studentProjectService.saveNewAvailableProject(project);

            System.out.println("--- [DEBUG FINAL: LISTENER] ---");
            System.out.println("Llamada al servicio completada sin errores desde el listener.");

        } catch (Exception e) {
            System.err.println("\n--- [DEBUG ERROR: LISTENER] ---");
            System.err.println("¡¡¡ Ocurrió un error al procesar el mensaje en el servicio !!!");
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------\n");
    }
}