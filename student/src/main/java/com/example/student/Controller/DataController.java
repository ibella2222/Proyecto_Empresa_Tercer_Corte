package com.example.student.Controller;


import com.example.student.DTO.ProjectDTO;
import com.example.student.Entities.Project;
import com.example.student.Entities.ProjectStatus;
import com.example.student.Entities.Student;
import com.example.student.Mapper.ProjectMapper;
import com.example.student.Repository.ProjectRepository;
import com.example.student.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/test-data")
public class DataController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateTestData() {
        Map<String, Object> result = new HashMap<>();

        studentRepository.deleteAll();
        projectRepository.deleteAll();

        List<Project> projects = createTestProjects();
        projectRepository.saveAll(projects);

        List<Student> students = createTestStudents(projects);
        studentRepository.saveAll(students);

        updateProjectsWithStudents(projects, students);
        projectRepository.saveAll(projects);

        result.put("message", "Datos de prueba generados correctamente");
        result.put("projects", ProjectMapper.toDTOList(projects));
        result.put("students", students);

        return ResponseEntity.ok(result);
    }

    private List<Project> createTestProjects() {
        List<Project> projects = new ArrayList<>();

        projects.add(new Project(UUID.randomUUID(), "Sistema de Gestión Académica", "Sistema para gestionar procesos académicos", "Optimizar inscripción y seguimiento", "Descripción 1", 8, new BigDecimal("15000.00"), LocalDate.now().minusMonths(2),
                Date.from(LocalDate.now().plusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()), "900456789-1", ProjectStatus.IN_EXECUTION, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        projects.add(new Project(UUID.randomUUID(), "Aplicación Móvil de Salud", "App móvil para hábitos saludables", "Registrar actividad física y salud", "Descripción 2", 6, new BigDecimal("12000.00"), LocalDate.now().minusMonths(1),
                Date.from(LocalDate.now().plusMonths(5).atStartOfDay(ZoneId.systemDefault()).toInstant()), "800123456-2", ProjectStatus.IN_EXECUTION, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        projects.add(new Project(UUID.randomUUID(), "Plataforma E-commerce", "Tienda virtual para artesanos", "Comercio electrónico para artesanos", "Descripción 3", 10, new BigDecimal("20000.00"), LocalDate.now().minusMonths(3),
                Date.from(LocalDate.now().plusMonths(7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "901234567-3", ProjectStatus.IN_EXECUTION, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        projects.add(new Project(UUID.randomUUID(), "Sistema IoT para Agricultura", "Monitoreo con sensores", "Optimizar cultivo con IoT", "Descripción 4", 12, new BigDecimal("25000.00"), LocalDate.now(),
                Date.from(LocalDate.now().plusMonths(12).atStartOfDay(ZoneId.systemDefault()).toInstant()), "860987654-4", ProjectStatus.RECEIVED, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        projects.add(new Project(UUID.randomUUID(), "Sistema de Gestión Documental", "Administra documentos", "Digitalización documental", "Descripción 5", 5, new BigDecimal("18000.00"), LocalDate.now().minusMonths(6),
                Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()), "830654321-5", ProjectStatus.CLOSED, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        return projects;
    }

    private List<Student> createTestStudents(List<Project> projects) {
        List<Student> students = new ArrayList<>();

        students.add(createStudent("juanperez", "Juan", "Pérez", "Ingeniería de Sistemas", projects.get(0)));
        students.add(createStudent("mariagomez", "María", "Gómez", "Ingeniería de Sistemas", projects.get(0)));
        students.add(createStudent("carlosrodriguez", "Carlos", "Rodríguez", "Ingeniería Biomédica", projects.get(1)));
        students.add(createStudent("analopez", "Ana", "López", "Ingeniería Biomédica", projects.get(1)));
        students.add(createStudent("pedroruiz", "Pedro", "Ruiz", "Ingeniería Informática", projects.get(2)));
        students.add(createStudent("luisadiaz", "Luisa", "Díaz", "Diseño Gráfico", projects.get(2)));
        students.add(createStudent("danielmartinez", "Daniel", "Martínez", "Ingeniería de Sistemas", projects.get(4)));
        students.add(createStudent("sofiagarcia", "Sofía", "García", "Ingeniería de Sistemas", projects.get(4)));
        students.add(createStudent("robertoalvarez", "Roberto", "Álvarez", "Ingeniería Electrónica", null));
        students.add(createStudent("lauracardenas", "Laura", "Cárdenas", "Ingeniería de Software", null));

        return students;
    }

    private Student createStudent(String username, String firstName, String lastName, String program, Project project) {
        Student student = new Student();
        student.setUsername(username);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setProgram(program);
        if (project != null) {
            student.setProjectId(project.getId().toString());
        }
        return student;
    }

    private void updateProjectsWithStudents(List<Project> projects, List<Student> students) {
        Map<String, List<String>> projectStudents = new HashMap<>();

        for (Student student : students) {
            if (student.getProjectId() != null && !student.getProjectId().isEmpty()) {
                projectStudents
                        .computeIfAbsent(student.getProjectId(), k -> new ArrayList<>())
                        .add(student.getFirstName() + " " + student.getLastName());
            }
        }

        for (Project project : projects) {
            String projectId = project.getId().toString();
            if (projectStudents.containsKey(projectId)) {
                project.setStudents(projectStudents.get(projectId));
            }
        }
    }
}
