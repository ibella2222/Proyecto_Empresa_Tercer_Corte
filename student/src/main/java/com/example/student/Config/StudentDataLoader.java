package com.example.student.Config;


import com.example.student.Entities.Project;
import com.example.student.Entities.Student;
import com.example.student.Repository.ProjectRepository;
import com.example.student.Repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class StudentDataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final ProjectRepository projectRepository;

    public StudentDataLoader(StudentRepository studentRepository,
                             ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            List<Project> proyectos = projectRepository.findAll();
            if (proyectos.size() < 2) {
                System.out.println("⚠ No hay suficientes proyectos para asignar estudiantes.");
                return;
            }

            Project proyectoIngenieria = proyectos.get(0);
            Project proyectoSalud = proyectos.get(1);

            Student estudianteIngenieria = new Student();
            estudianteIngenieria.setUsername("est_ingenieria");
            estudianteIngenieria.setFirstName("Carlos");
            estudianteIngenieria.setLastName("Rodríguez");
            estudianteIngenieria.setProgram("Ingeniería de Sistemas");
            estudianteIngenieria.setProjectId(proyectoIngenieria.getId().toString());

            Student estudianteMedicina = new Student();
            estudianteMedicina.setUsername("est_medicina");
            estudianteMedicina.setFirstName("María");
            estudianteMedicina.setLastName("Gómez");
            estudianteMedicina.setProgram("Medicina");
            estudianteMedicina.setProjectId(proyectoSalud.getId().toString());

            studentRepository.saveAll(List.of(estudianteIngenieria, estudianteMedicina));

            System.out.println("✅ Estudiantes creados y asignados:");
            System.out.println("- " + estudianteIngenieria.getFirstName() + " -> " + proyectoIngenieria.getName());
            System.out.println("- " + estudianteMedicina.getFirstName() + " -> " + proyectoSalud.getName());
        }
    }
}
