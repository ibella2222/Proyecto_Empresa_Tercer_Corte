package com.example.coordination.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.coordination.adapter.repository.StudentRepository;
import com.example.coordination.domain.model.Student;
import com.example.coordination.dto.StudentDTO;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> getStudentsByProjectId(String projectId) {
        List<Student> students = studentRepository.findByProjectId(projectId);
        return students.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getUsername(),
                student.getFirstName(),
                student.getLastName(),
                student.getProgram(),
                student.getProjectId()
        );
    }
}
