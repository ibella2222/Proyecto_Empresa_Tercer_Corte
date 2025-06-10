package com.example.company.repository;

import com.example.company.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByCompanyNIT(String companyNIT);
    // --- AÑADE ESTE NUEVO MÉTODO ---
    /**
     * Busca todos los proyectos que coincidan con un estado específico.
     * @param state El estado a buscar (ej: "ACCEPTED", "RECEIVED").
     * @return Una lista de proyectos que coinciden con el estado.
     */
    List<Project> findByState(String state);
}
