package com.example.student.Infrastructure.Adapter.Out.Persistence;

import com.example.student.Domain.Model.Student;
import com.example.student.Domain.Port.Out.StudentRepositoryPort;
import com.example.student.Infrastructure.Adapter.Out.Persistence.Entity.StudentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component // O @Repository
public class JpaStudentRepositoryAdapter implements StudentRepositoryPort {

    private final SpringDataStudentRepository studentRepository;

    public JpaStudentRepositoryAdapter(SpringDataStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Mappers (se podrían mover a una clase Mapper dedicada)
    private StudentEntity toEntity(Student student) {
        return new StudentEntity(student.getId(), student.getUsername(), student.getFirstName(), student.getLastName(), student.getProgram(), student.getProjectId());
    }

    private Student toDomain(StudentEntity entity) {
        return new Student(entity.getId(), entity.getUsername(), entity.getFirstName(), entity.getLastName(), entity.getProgram());
    }

    @Override
    public Student save(Student student) {
        StudentEntity entity = toEntity(student);
        StudentEntity savedEntity = studentRepository.save(entity);
        // Aquí no es necesario volver a convertir a dominio si el método retorna void o si la firma es consistente
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Student> findById(String id) {
        return studentRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Student> findByProjectId(String projectId) {
        return studentRepository.findByProjectId(projectId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdAndProjectId(String studentId, String projectId) {
        return studentRepository.existsByIdAndProjectId(studentId, projectId);
    }
}