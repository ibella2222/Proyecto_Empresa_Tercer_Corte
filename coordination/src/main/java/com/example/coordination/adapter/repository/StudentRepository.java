package com.example.coordination.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coordination.domain.model.Student;



public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByProjectId(String projectId);
}