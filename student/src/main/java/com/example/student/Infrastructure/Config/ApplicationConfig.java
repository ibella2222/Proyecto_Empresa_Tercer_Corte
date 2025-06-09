package com.example.student.Infrastructure.Config;



import com.example.student.Domain.Port.Out.StudentRepositoryPort;
import com.example.student.Domain.Service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public StudentService studentService(StudentRepositoryPort studentRepositoryPort) {
        return new StudentService(studentRepositoryPort);
    }
}
