package com.example.login.Messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.login.DTO.CompanyDTO;

@Component
public class CompanyRegistrationSender {
    
    private static final Logger logger = LoggerFactory.getLogger(CompanyRegistrationSender.class);
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.queue.companyRegistration}")
    private String companyQueue;
    
    public CompanyRegistrationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendCompanyRegistration(CompanyDTO companyDTO) {
        try {
            logger.info("Enviando mensaje de registro de empresa a la cola: {}", companyQueue);
            logger.info("Datos de la empresa: {}", companyDTO);
            
            rabbitTemplate.convertAndSend(companyQueue, companyDTO);
            
            logger.info("Mensaje enviado correctamente a la cola");
        } catch (AmqpException e) {
            logger.error("Error al enviar mensaje a la cola RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar mensaje a la cola: " + e.getMessage(), e);
        }
    }
}