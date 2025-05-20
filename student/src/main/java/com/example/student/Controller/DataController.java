package com.example.student.Controller;

import com.example.student.DTO.ProjectDTO;
import com.example.student.Entities.Project;
import com.example.student.Entities.ProjectStatus;
import com.example.student.Entities.Student;
import com.example.student.Entities.User;
import com.example.student.Mapper.ProjectMapper;
import com.example.student.Repository.ProjectRepository;
import com.example.student.Repository.StudentRepository;
import com.example.student.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Controlador para generar datos de prueba
 */
@RestController
@RequestMapping("/api/test-data")
public class DataController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Genera datos de prueba para la aplicación
     * @return Resumen de los datos generados
     */
    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateTestData() {
        Map<String, Object> result = new HashMap<>();

        // Limpiar datos existentes para evitar duplicados
        studentRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        // Crear proyectos de prueba
        List<Project> projects = createTestProjects();
        projectRepository.saveAll(projects);

        // Crear usuarios y estudiantes
        List<User> users = createTestUsers();
        userRepository.saveAll(users);

        List<Student> students = createTestStudents(projects);
        studentRepository.saveAll(students);

        // Actualizar proyectos con estudiantes asociados
        updateProjectsWithStudents(projects, students);
        projectRepository.saveAll(projects);

        // Preparar respuesta
        result.put("message", "Datos de prueba generados correctamente");
        result.put("projects", ProjectMapper.toDTOList(projects));
        result.put("users", users.size());
        result.put("students", students);

        return ResponseEntity.ok(result);
    }

    /**
     * Crea proyectos de prueba
     * @return Lista de proyectos
     */
    private List<Project> createTestProjects() {
        List<Project> projects = new ArrayList<>();

        // Proyecto 1
        Project project1 = new Project();
        project1.setId(UUID.randomUUID());
        project1.setName("Sistema de Gestión Académica");
        project1.setSummary("Sistema para gestionar procesos académicos universitarios");
        project1.setObjectives("Optimizar los procesos de inscripción, calificación y seguimiento de estudiantes");
        project1.setDescription("Este sistema permitirá a los administradores gestionar inscripciones, " +
                "calificaciones, horarios y asistencia de estudiantes. Los estudiantes podrán " +
                "consultar sus notas, horarios y hacer solicitudes académicas.");
        project1.setMaxMonths(8);
        project1.setBudget(new BigDecimal("15000.00"));
        project1.setStartDate(LocalDate.now().minusMonths(2));
        project1.setFinalizationDate(Date.from(LocalDate.now().plusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setCompanyNIT("900456789-1");
        project1.setStatus(ProjectStatus.IN_EXECUTION);
        project1.setComments(Arrays.asList(
                "Primera revisión completada con éxito",
                "Se requieren ajustes en el módulo de reportes"
        ));
        projects.add(project1);

        // Proyecto 2
        Project project2 = new Project();
        project2.setId(UUID.randomUUID());
        project2.setName("Aplicación Móvil de Salud");
        project2.setSummary("App móvil para seguimiento de hábitos saludables");
        project2.setObjectives("Desarrollar una aplicación que permita a los usuarios rastrear su actividad física, alimentación y estado de salud");
        project2.setDescription("Aplicación móvil multiplataforma que permite a los usuarios registrar actividad física, " +
                "hábitos alimenticios, sueño y otros indicadores de salud. Incluye tableros estadísticos " +
                "y recomendaciones personalizadas.");
        project2.setMaxMonths(6);
        project2.setBudget(new BigDecimal("12000.00"));
        project2.setStartDate(LocalDate.now().minusMonths(1));
        project2.setFinalizationDate(Date.from(LocalDate.now().plusMonths(5).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project2.setCompanyNIT("800123456-2");
        project2.setStatus(ProjectStatus.IN_EXECUTION);
        project2.setComments(Arrays.asList(
                "Diseño de interfaz aprobado",
                "En desarrollo el módulo de estadísticas"
        ));
        projects.add(project2);

        // Proyecto 3
        Project project3 = new Project();
        project3.setId(UUID.randomUUID());
        project3.setName("Plataforma E-commerce");
        project3.setSummary("Tienda virtual para productos artesanales");
        project3.setObjectives("Crear una plataforma de comercio electrónico para artesanos locales");
        project3.setDescription("Plataforma web que permitirá a los artesanos locales vender sus productos " +
                "a nivel nacional. Incluye catálogo de productos, carrito de compras, sistema de pagos " +
                "y gestión de envíos.");
        project3.setMaxMonths(10);
        project3.setBudget(new BigDecimal("20000.00"));
        project3.setStartDate(LocalDate.now().minusMonths(3));
        project3.setFinalizationDate(Date.from(LocalDate.now().plusMonths(7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project3.setCompanyNIT("901234567-3");
        project3.setStatus(ProjectStatus.IN_EXECUTION);
        project3.setComments(Arrays.asList(
                "Integración con pasarela de pagos completada",
                "Pendiente implementación del sistema de envíos"
        ));
        projects.add(project3);

        // Proyecto 4 (recibido pero no iniciado)
        Project project4 = new Project();
        project4.setId(UUID.randomUUID());
        project4.setName("Sistema IoT para Agricultura");
        project4.setSummary("Sistema de monitoreo para cultivos usando Internet de las Cosas");
        project4.setObjectives("Implementar un sistema de sensores y monitoreo remoto para optimizar el cultivo");
        project4.setDescription("Sistema basado en sensores IoT para monitorear condiciones ambientales " +
                "y del suelo en cultivos agrícolas. Incluye aplicación web y móvil para visualizar datos " +
                "y recibir alertas.");
        project4.setMaxMonths(12);
        project4.setBudget(new BigDecimal("25000.00"));
        project4.setStartDate(LocalDate.now());
        project4.setFinalizationDate(Date.from(LocalDate.now().plusMonths(12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project4.setCompanyNIT("860987654-4");
        project4.setStatus(ProjectStatus.RECEIVED);
        project4.setComments(Arrays.asList(
                "Propuesta recibida, pendiente de revisión"
        ));
        projects.add(project4);

        // Proyecto 5 (finalizado)
        Project project5 = new Project();
        project5.setId(UUID.randomUUID());
        project5.setName("Sistema de Gestión Documental");
        project5.setSummary("Sistema para administración y control de documentos");
        project5.setObjectives("Digitalizar y optimizar la gestión documental de la organización");
        project5.setDescription("Sistema web para la digitalización, almacenamiento, búsqueda y control " +
                "de documentos corporativos. Incluye control de versiones, firma digital y flujos de aprobación.");
        project5.setMaxMonths(5);
        project5.setBudget(new BigDecimal("18000.00"));
        project5.setStartDate(LocalDate.now().minusMonths(6));
        project5.setFinalizationDate(Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project5.setCompanyNIT("830654321-5");
        project5.setStatus(ProjectStatus.CLOSED);
        project5.setComments(Arrays.asList(
                "Proyecto completado satisfactoriamente",
                "Cliente ha solicitado nueva fase para implementar funcionalidades adicionales"
        ));
        projects.add(project5);

        return projects;
    }

    /**
     * Crea usuarios de prueba
     * @return Lista de usuarios
     */
    private List<User> createTestUsers() {
        List<User> users = new ArrayList<>();

        // Usuario administrador
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("password");  // En producción usar BCrypt u otro método de encriptación
        admin.setRole("ADMIN");
        admin.setProfileCompleted(true);
        users.add(admin);

        // Usuario coordinador
        User coordinator = new User();
        coordinator.setUsername("coordinator");
        coordinator.setPassword("password");
        coordinator.setRole("COORDINATOR");
        coordinator.setProfileCompleted(true);
        users.add(coordinator);

        // Usuario empresa
        User company = new User();
        company.setUsername("company");
        company.setPassword("password");
        company.setRole("COMPANY");
        company.setProfileCompleted(true);
        users.add(company);

        return users;
    }

    /**
     * Crea estudiantes de prueba asociados a proyectos
     * @param projects Lista de proyectos a los que se asociarán los estudiantes
     * @return Lista de estudiantes
     */
    private List<Student> createTestStudents(List<Project> projects) {
        List<Student> students = new ArrayList<>();

        // Estudiante 1 - asociado al proyecto 1
        Student student1 = new Student();
        student1.setUsername("juanperez");
        student1.setPassword("password");
        student1.setRole("STUDENT");
        student1.setProfileCompleted(true);
        student1.setFirstName("Juan");
        student1.setLastName("Pérez");
        student1.setProgram("Ingeniería de Sistemas");
        student1.setProjectId(projects.get(0).getId().toString());
        students.add(student1);

        // Estudiante 2 - asociado al proyecto 1
        Student student2 = new Student();
        student2.setUsername("mariagomez");
        student2.setPassword("password");
        student2.setRole("STUDENT");
        student2.setProfileCompleted(true);
        student2.setFirstName("María");
        student2.setLastName("Gómez");
        student2.setProgram("Ingeniería de Sistemas");
        student2.setProjectId(projects.get(0).getId().toString());
        students.add(student2);

        // Estudiante 3 - asociado al proyecto 2
        Student student3 = new Student();
        student3.setUsername("carlosrodriguez");
        student3.setPassword("password");
        student3.setRole("STUDENT");
        student3.setProfileCompleted(true);
        student3.setFirstName("Carlos");
        student3.setLastName("Rodríguez");
        student3.setProgram("Ingeniería Biomédica");
        student3.setProjectId(projects.get(1).getId().toString());
        students.add(student3);

        // Estudiante 4 - asociado al proyecto 2
        Student student4 = new Student();
        student4.setUsername("analopez");
        student4.setPassword("password");
        student4.setRole("STUDENT");
        student4.setProfileCompleted(true);
        student4.setFirstName("Ana");
        student4.setLastName("López");
        student4.setProgram("Ingeniería Biomédica");
        student4.setProjectId(projects.get(1).getId().toString());
        students.add(student4);

        // Estudiante 5 - asociado al proyecto 3
        Student student5 = new Student();
        student5.setUsername("pedroruiz");
        student5.setPassword("password");
        student5.setRole("STUDENT");
        student5.setProfileCompleted(true);
        student5.setFirstName("Pedro");
        student5.setLastName("Ruiz");
        student5.setProgram("Ingeniería Informática");
        student5.setProjectId(projects.get(2).getId().toString());
        students.add(student5);

        // Estudiante 6 - asociado al proyecto 3
        Student student6 = new Student();
        student6.setUsername("luisadiaz");
        student6.setPassword("password");
        student6.setRole("STUDENT");
        student6.setProfileCompleted(true);
        student6.setFirstName("Luisa");
        student6.setLastName("Díaz");
        student6.setProgram("Diseño Gráfico");
        student6.setProjectId(projects.get(2).getId().toString());
        students.add(student6);

        // Estudiante 7 - asociado al proyecto 5 (finalizado)
        Student student7 = new Student();
        student7.setUsername("danielmartinez");
        student7.setPassword("password");
        student7.setRole("STUDENT");
        student7.setProfileCompleted(true);
        student7.setFirstName("Daniel");
        student7.setLastName("Martínez");
        student7.setProgram("Ingeniería de Sistemas");
        student7.setProjectId(projects.get(4).getId().toString());
        students.add(student7);

        // Estudiante 8 - asociado al proyecto 5 (finalizado)
        Student student8 = new Student();
        student8.setUsername("sofiagarcia");
        student8.setPassword("password");
        student8.setRole("STUDENT");
        student8.setProfileCompleted(true);
        student8.setFirstName("Sofía");
        student8.setLastName("García");
        student8.setProgram("Ingeniería de Sistemas");
        student8.setProjectId(projects.get(4).getId().toString());
        students.add(student8);

        // Estudiante 9 - sin proyecto asignado
        Student student9 = new Student();
        student9.setUsername("robertoalvarez");
        student9.setPassword("password");
        student9.setRole("STUDENT");
        student9.setProfileCompleted(true);
        student9.setFirstName("Roberto");
        student9.setLastName("Álvarez");
        student9.setProgram("Ingeniería Electrónica");
        students.add(student9);

        // Estudiante 10 - sin proyecto asignado
        Student student10 = new Student();
        student10.setUsername("lauracardenas");
        student10.setPassword("password");
        student10.setRole("STUDENT");
        student10.setProfileCompleted(true);
        student10.setFirstName("Laura");
        student10.setLastName("Cárdenas");
        student10.setProgram("Ingeniería de Software");
        students.add(student10);

        return students;
    }

    /**
     * Actualiza los proyectos con los estudiantes asociados
     * @param projects Lista de proyectos
     * @param students Lista de estudiantes
     */
    private void updateProjectsWithStudents(List<Project> projects, List<Student> students) {
        // Crear un mapa para agrupar estudiantes por proyecto
        Map<String, List<String>> projectStudents = new HashMap<>();

        // Agrupar estudiantes por proyecto
        for (Student student : students) {
            if (student.getProjectId() != null && !student.getProjectId().isEmpty()) {
                if (!projectStudents.containsKey(student.getProjectId())) {
                    projectStudents.put(student.getProjectId(), new ArrayList<>());
                }
                projectStudents.get(student.getProjectId()).add(student.getFirstName() + " " + student.getLastName());
            }
        }

        // Asignar estudiantes a los proyectos
        for (Project project : projects) {
            String projectId = project.getId().toString();
            if (projectStudents.containsKey(projectId)) {
                project.setStudents(projectStudents.get(projectId));
            }
        }
    }
}