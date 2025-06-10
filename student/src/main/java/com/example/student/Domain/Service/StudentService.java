package com.example.student.Domain.Service;

import com.example.student.Domain.Model.Student;
import com.example.student.Domain.Port.In.StudentUseCase;
import com.example.student.Domain.Port.Out.StudentRepositoryPort;

import java.util.List;
import java.util.Optional;

public class StudentService implements StudentUseCase {

    private final StudentRepositoryPort studentRepositoryPort;

    public StudentService(StudentRepositoryPort studentRepositoryPort) {
        this.studentRepositoryPort = studentRepositoryPort;
    }

    @Override
    public Student applyToProject(String studentId, String projectId) {
        if (studentRepositoryPort.existsByIdAndProjectId(studentId, projectId)) {
            throw new IllegalStateException("Ya estás postulado a este proyecto.");
        }

        Student student = studentRepositoryPort.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Estudiante no encontrado."));

        student.setProjectId(projectId);
        return studentRepositoryPort.save(student);
    }

    @Override
    public void cancelApplication(String studentId, String projectId) {
        Student student = studentRepositoryPort.findById(studentId)
                .filter(s -> projectId.equals(s.getProjectId()))
                .orElseThrow(() -> new IllegalStateException("No se encontró la postulación para cancelar."));

        student.setProjectId(null);
        studentRepositoryPort.save(student);
    }

    @Override
    public Optional<Student> getStudentById(String studentId) {
        return studentRepositoryPort.findById(studentId);
    }

    @Override
    public List<Student> getStudentsByProjectId(String projectId) {
        return studentRepositoryPort.findByProjectId(projectId);
    }

    @Override
    public Student updateStudentProfile(String studentId, String firstName, String lastName, String program) {
        Student student = studentRepositoryPort.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Estudiante no encontrado."));

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setProgram(program);

        return studentRepositoryPort.save(student);
    }
    @Override
    public Student createOrUpdateProfile(String studentId, String username, String firstName, String lastName, String program) {
        // Busca al estudiante. Si no existe, crea una nueva instancia.
        Student student = studentRepositoryPort.findById(studentId)
                .orElse(new Student(studentId, username, firstName, lastName, program));

        // Actualiza los datos con la información recibida
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setProgram(program);

        return studentRepositoryPort.save(student);
    }
}