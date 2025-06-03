/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.Project;
import co.edu.unicauca.proyectocurso.domain.entities.ProjectStats;
import java.util.List;

/**
 *
 * @author Lenovo pc
 */
public interface IProjectStatsRepository {
        /**
     * Obtiene la lista de todos los proyectos para las estadisticas registrados.
     *
     * @return Lista de proyectos para las estadisticas.
     */
    List<ProjectStats> findAll();
}
