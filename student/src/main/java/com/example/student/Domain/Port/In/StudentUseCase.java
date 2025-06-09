package com.example.student.Domain.Port.In;


import com.example.student.Domain.Model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentUseCase {
    Student applyToProject(String studentId, String projectId);
    void cancelApplication(String studentId, String projectId);
    Optional<Student> getStudentById(String studentId);
    List<Student> getStudentsByProjectId(String projectId);
    Student updateStudentProfile(String studentId, String firstName, String lastName, String program);
    Student createOrUpdateProfile(String studentId, String username, String firstName, String lastName, String program);
}
