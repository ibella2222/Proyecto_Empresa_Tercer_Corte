package com.example.student.Repository;



import com.example.student.Domain.Model.StudentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentProjectRepository extends JpaRepository<StudentProject, UUID> {
    // Spring Data JPA nos da los métodos save(), findById(), etc., automáticamente.
}