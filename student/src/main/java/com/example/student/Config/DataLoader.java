package com.example.student.Config;
import com.example.student.Entities.Project;
import com.example.student.Entities.ProjectStatus;
import com.example.student.Repository.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class DataLoader implements CommandLineRunner {

    private final ProjectRepository projectRepository;

    public DataLoader(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (projectRepository.count() == 0) {
            // Proyecto 1 - Creado manualmente
            Project proyectoIngenieria = new Project();
            proyectoIngenieria.setName("Desarrollo Plataforma Educativa");
            proyectoIngenieria.setSummary("Sistema de gestión de aprendizaje en línea");
            proyectoIngenieria.setObjectives("Digitalizar procesos educativos");
            proyectoIngenieria.setDescription("Plataforma con cursos, evaluaciones y seguimiento académico");
            proyectoIngenieria.setMaxMonths(12);
            proyectoIngenieria.setBudget(new BigDecimal("45000.00"));
            proyectoIngenieria.setStartDate(LocalDate.now().plusMonths(1));
            proyectoIngenieria.setCompanyNIT("901234567-1");
            proyectoIngenieria.setStatus(ProjectStatus.IN_EXECUTION);
            proyectoIngenieria.setComments(Arrays.asList("Prioridad alta", "Equipo asignado"));

            // Proyecto 2 - Creado manualmente
            Project proyectoSalud = new Project();
            proyectoSalud.setName("Sistema Historia Clínica Digital");
            proyectoSalud.setSummary("Gestión electrónica de registros médicos");
            proyectoSalud.setObjectives("Reducir uso de papel en hospitales");
            proyectoSalud.setDescription("Sistema seguro para almacenamiento y consulta de historias clínicas");
            proyectoSalud.setMaxMonths(18);
            proyectoSalud.setBudget(new BigDecimal("68000.00"));
            proyectoSalud.setStartDate(LocalDate.now().plusMonths(2));
            proyectoSalud.setCompanyNIT("890123456-2");
            proyectoSalud.setStatus(ProjectStatus.RECEIVED);
            proyectoSalud.setComments(List.of("En fase de aprobación"));

            projectRepository.saveAll(Arrays.asList(proyectoIngenieria, proyectoSalud));

            System.out.println("Proyectos creados manualmente:");
            System.out.println("1. " + proyectoIngenieria.getName());
            System.out.println("2. " + proyectoSalud.getName());
        }
    }
}