package com.example.login.Messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.login.DTO.StudentDTO;

@Component
public class StudentRegistrationSender {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentRegistrationSender.class);
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.queue.studentRegistration}")
    private String studentQueue;
    
    public StudentRegistrationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendStudentRegistration(StudentDTO studentDTO) {
        try {
            logger.info("Enviando mensaje de registro de estudiante a la cola: {}", studentQueue);
            logger.info("Datos del estudiante: {}", studentDTO);
            
            rabbitTemplate.convertAndSend(studentQueue, studentDTO);
            
            logger.info("Mensaje enviado correctamente a la cola");
        } catch (AmqpException e) {
            logger.error("Error al enviar mensaje a la cola RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar mensaje a la cola: " + e.getMessage(), e);
        }
    }
}