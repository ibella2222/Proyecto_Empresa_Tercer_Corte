package com.example.login.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.login.Entities.User;

import jakarta.persistence.LockModeType;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
    
    
    boolean existsByUsername(String username);
    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithLock(@Param("id") int id);
}