package com.example.company.repository;

import java.util.Optional;
import com.example.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    boolean existsByNit(String nit);
    // Agregar este método para buscar por username
    Optional<Company> findByUsername(String username);
}