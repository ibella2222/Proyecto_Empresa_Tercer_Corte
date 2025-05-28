package com.example.coordination.adapter.messaging;

import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.dto.ProjectEvent;
import com.example.coordination.infrastructure.RabbitMQConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProjectEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProjectStateChange(UUID projectId, String projectName, String summary, String objectives,
                                       String description, int maxDurationMonths, BigDecimal budget,
                                       LocalDate startDate, String companyNIT, ProjectStateEnum newState) {
        ProjectEvent event = new ProjectEvent(
                projectId,
                projectName,
                summary,
                objectives,
                description,
                maxDurationMonths,
                budget,
                startDate,
                companyNIT,
                newState.toString()
        );

        // Routing key que hará que el mensaje llegue a 'coordinator.to.company.queue'
        String routingKey = RabbitMQConfig.COORDINATOR_ROUTING_KEY;

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMPANY_EXCHANGE,
                routingKey,
                event
        );

        System.out.println("Mensaje enviado a RabbitMQ con routing key [" + routingKey + "] en exchange [" + RabbitMQConfig.COMPANY_EXCHANGE + "]: " + event);
    }
}