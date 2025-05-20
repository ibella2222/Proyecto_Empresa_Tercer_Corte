package com.example.login.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.login.Entities.Student;
import com.example.login.Entities.User;

import jakarta.persistence.LockModeType;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    // Métodos adicionales si se requieren
    boolean existsByUsername(String username);
    
    Optional<User> findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Student s WHERE s.id = :id")
    Optional<Student> findByIdWithLock(@Param("id") int id);
}