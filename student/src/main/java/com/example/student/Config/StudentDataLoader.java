package com.example.student.Config;


import com.example.student.Entities.Project;
import com.example.student.Entities.Student;
import com.example.student.Entities.User;
import com.example.student.Repository.ProjectRepository;
import com.example.student.Repository.StudentRepository;
import com.example.student.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public void run(String... args) throws Exception {
        if (studentRepository.count() == 0) {
            List<Project> proyectos = projectRepository.findAll();

            Project proyectoIngenieria = proyectos.get(0);
            Project proyectoSalud = proyectos.get(1);

            // Estudiante 1 - Creado manualmente
            Student estudianteIngenieria = new Student();
            estudianteIngenieria.setUsername("est_ingenieria");
            estudianteIngenieria.setPassword("pass123");
            estudianteIngenieria.setRole("STUDENT");
            estudianteIngenieria.setProfileCompleted(true);
            estudianteIngenieria.setFirstName("Carlos");
            estudianteIngenieria.setLastName("Rodríguez");
            estudianteIngenieria.setProgram("Ingeniería de Sistemas");
            estudianteIngenieria.setProjectId(proyectoIngenieria.getId().toString());

            // Estudiante 2 - Creado manualmente
            Student estudianteMedicina = new Student();
            estudianteMedicina.setUsername("est_medicina");
            estudianteMedicina.setPassword("pass456");
            estudianteMedicina.setRole("STUDENT");
            estudianteMedicina.setProfileCompleted(false);
            estudianteMedicina.setFirstName("María");
            estudianteMedicina.setLastName("Gómez");
            estudianteMedicina.setProgram("Medicina");
            estudianteMedicina.setProjectId(proyectoSalud.getId().toString());

            studentRepository.saveAll(List.of(estudianteIngenieria, estudianteMedicina));

            System.out.println("Estudiantes creados manualmente y asignados:");
            System.out.println("- " + estudianteIngenieria.getFirstName() + " -> " + proyectoIngenieria.getName());
            System.out.println("- " + estudianteMedicina.getFirstName() + " -> " + proyectoSalud.getName());
        }
    }
}