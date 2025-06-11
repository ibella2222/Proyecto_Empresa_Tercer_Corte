package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.ProjectStats; // Asegúrate de que esta clase exista en el cliente
import java.util.List;

public interface IStatsRepository {
    /**
     * Obtiene todas las estadísticas de proyectos desde el backend.
     * @return Una lista de estadísticas de proyectos.
     */
    List<ProjectStats> findAll();
}