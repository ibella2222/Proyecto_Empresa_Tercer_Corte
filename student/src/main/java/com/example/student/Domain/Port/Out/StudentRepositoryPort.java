package com.example.student.Domain.Port.Out;


import com.example.student.Domain.Model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepositoryPort {
    Student save(Student student);
    Optional<Student> findById(String id);
    List<Student> findByProjectId(String projectId);
    boolean existsByIdAndProjectId(String studentId, String projectId);
}
