package com.example.company.repository;

import com.example.company.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByCompanyNIT(String companyNIT);
}
