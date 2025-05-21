package com.example.company.config;

import com.example.company.entity.Company;
import com.example.company.entity.Project;
import com.example.company.repository.CompanyRepository;
import com.example.company.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader {


    private final ProjectRepository projectRepository;

    public DataLoader(CompanyRepository companyRepository, ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostConstruct
    public void init() {


        if (projectRepository.count() == 0) {
            // Proyecto para empresa 1
            Project p1 = new Project();
            p1.setName("Sistema de Gestión");
            p1.setSummary("Software ERP");
            p1.setObjectives("Optimizar procesos empresariales");
            p1.setDescription("Desarrollar un ERP personalizado");
            p1.setDate(LocalDate.now());
            p1.setFinalizationDate(LocalDate.of(2024, 12, 31));
            p1.setCompanyNIT("3");
            p1.setBudget(800000);
            p1.setMaxMonths(12);
            p1.setState("RECEIVED");


            projectRepository.save(p1);

            System.out.println("📦 Proyectos precargados en la base de datos.");
        }
    }
}
