package com.example.login.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.login.Entities.Company;

import jakarta.persistence.LockModeType;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    
    // Método para buscar por contactPhone
    Optional<Company> findByContactPhone(String contactPhone);
    
    // Método para buscar por NIT
    Optional<Company> findByNit(String nit);
    
    // Verificar si existe un NIT
    boolean existsByNit(String nit);
    
    // Método para buscar por nombre de empresa
    Optional<Company> findByName(String name);
    
    // Este método busca empresas por userId (asumiendo que Company extiende User)
    // La consulta ha sido revisada para asegurar que funcione correctamente
    @Query("SELECT c FROM Company c WHERE c.id = :userId")
    Optional<Company> findByUserId(@Param("userId") int userId);
    
    // Versión alternativa si la relación entre Company y User es diferente
    // @Query("SELECT c FROM Company c JOIN c.user u WHERE u.id = :userId")
    // Optional<Company> findByUserId(@Param("userId") int userId);
    
    // Método con bloqueo pesimista para operaciones transaccionales
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Company c WHERE c.id = :id")
    Optional<Company> findByIdWithLock(@Param("id") int id);
}