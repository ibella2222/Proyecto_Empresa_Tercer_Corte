/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.proyectocurso.domain.services;

import co.edu.unicauca.proyectocurso.access.IProjectStatsRepository;
import co.edu.unicauca.proyectocurso.domain.entities.ProjectStats;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Lenovo pc
 */
public class ProjectStatsService {
    private final IProjectStatsRepository repository;

    // Constructor con inyección de dependencias
    public ProjectStatsService(IProjectStatsRepository repository) {
        this.repository = repository;
    }
    
    
    public List<ProjectStats> findByDateRange(LocalDate start, LocalDate end) {
        return repository.findAll().stream()
                .filter(stat -> {
                    LocalDate date = stat.getChangeDate();
                    return date != null && !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }
}






