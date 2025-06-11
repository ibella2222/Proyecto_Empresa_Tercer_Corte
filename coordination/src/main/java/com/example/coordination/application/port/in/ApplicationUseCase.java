package com.example.coordination.application.port.in;

import com.example.coordination.dto.ApplicationDTO;
import java.util.List;

/**
 * Puerto de entrada que define los casos de uso para la gestión de postulaciones.
 */
public interface ApplicationUseCase {

    /**
     * Obtiene una lista de todas las postulaciones de estudiantes que están pendientes
     * de aprobación o rechazo.
     * @return Una lista de DTOs de postulaciones.
     */
    List<ApplicationDTO> getPendingApplications();

    /**
     * Procesa la aprobación de la postulación de un estudiante a un proyecto.
     * @param studentId El ID del estudiante.
     * @param projectId El ID del proyecto.
     */
    void approveApplication(String studentId, String projectId);

    /**
     * Procesa el rechazo de la postulación de un estudiante a un proyecto.
     * @param studentId El ID del estudiante.
     * @param projectId El ID del proyecto.
     */
    void rejectApplication(String studentId, String projectId);
}