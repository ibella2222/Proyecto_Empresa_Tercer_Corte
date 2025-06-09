package com.example.student.Messaging;

import com.example.student.DTO.StudentDTO;
import com.example.student.Entities.Student;
import com.example.student.Repository.StudentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StudentConsumer {

    private final StudentRepository studentRepository;

    public StudentConsumer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @RabbitListener(queues = "student.queue")
    public void receiveStudent(StudentDTO dto) {
        System.out.println("Recibido estudiante por cola: " + dto);

        Student student = new Student();
        student.setUsername(dto.getUsername());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setProgram(dto.getProgram());

        studentRepository.save(student);
    }
}
