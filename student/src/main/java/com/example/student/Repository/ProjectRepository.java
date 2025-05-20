package com.example.student.Repository;

import com.example.student.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    /**
     * Busca un proyecto por su nombre
     * @param name Nombre del proyecto a buscar
     * @return Proyecto encontrado o null si no existe
     */
    Project findByName(String name);
}