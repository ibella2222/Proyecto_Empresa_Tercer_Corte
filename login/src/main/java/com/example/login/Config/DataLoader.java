package com.example.login.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.login.Entities.Company;
import com.example.login.Entities.Student;
import com.example.login.Entities.User;
import com.example.login.Repository.CompanyRepository;
import com.example.login.Repository.StudentRepository;
import com.example.login.Repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public DataLoader(UserRepository userRepository,
                      StudentRepository studentRepository,
                      CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) {
        // Usuario administrador
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123"); // Puedes usar codificador si lo necesitas
            admin.setRole("Admin");
            admin.setProfileCompleted(true);
            userRepository.save(admin);
        }

        // Usuario estudiante
        if (!userRepository.existsByUsername("estudiante1")) {
            Student student = new Student();
            student.setUsername("estudiante1");
            student.setPassword("pass123");
            student.setRole("Estudiante");
            student.setProfileCompleted(true);
            student.setFirstName("Laura");
            student.setLastName("Rodríguez");
            student.setProgram("Ingeniería Informática");
            student.setProjectId("PROJ-101");
            studentRepository.save(student);
        }

        // Usuario empresa
        if (!userRepository.existsByUsername("empresa1")) {
            Company company = new Company();
            company.setUsername("empresa1");
            company.setPassword("empresa123");
            company.setRole("Empresa");
            company.setProfileCompleted(true);
            company.setNit("900123456");
            company.setName("SoftTech S.A.");
            company.setSector("Tecnología");
            company.setContactPhone("3115678901");
            company.setContactFirstName("Carlos");
            company.setContactLastName("Mendoza");
            company.setContactPosition("Gerente de Talento");
            companyRepository.save(company);
        }
    }
}
