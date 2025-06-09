package com.example.student.Infrastructure.Adapter.Out.Persistence;

import com.example.student.Infrastructure.Adapter.Out.Persistence.Entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataStudentRepository extends JpaRepository<StudentEntity, String> {
    List<StudentEntity> findByProjectId(String projectId);
    boolean existsByIdAndProjectId(String id, String projectId);
}
