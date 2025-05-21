package com.example.login.Messaging;

import com.example.login.DTO.CompanyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CompanyRegistrationSender {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRegistrationSender.class);
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "company.exchange";
    private static final String ROUTING_KEY = "company.login.routingkey";

    public CompanyRegistrationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCompanyRegistration(CompanyDTO companyDTO) {
        try {
            logger.info(" Enviando CompanyDTO a '{}': {}", ROUTING_KEY, companyDTO);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, companyDTO); // ✅ ENVÍAS EL OBJETO
            logger.info(" Mensaje enviado correctamente al exchange '{}'", EXCHANGE);
        } catch (AmqpException e) {
            logger.error(" Error al enviar mensaje a RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar mensaje a la cola: " + e.getMessage(), e);
        }
    }
}
