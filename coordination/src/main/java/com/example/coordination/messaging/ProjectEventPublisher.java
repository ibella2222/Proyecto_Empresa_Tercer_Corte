package com.example.coordination.messaging;

import com.example.coordination.config.RabbitMQConfig;
import com.example.coordination.dto.ProjectEvent;
import com.example.coordination.entity.ProjectStateEnum;

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

    public void sendProjectStateChange(UUID projectId, String projectName,String summary, String objectives, String description, int maxDurationMonths, BigDecimal budget, LocalDate startDate, String companyNIT, ProjectStateEnum newState) {
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

        // Usa una sola routing key que coincida con el comodín definido en la configuración
        String routingKey = "project." + newState.name().toLowerCase();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROJECT_EVENT_EXCHANGE,
                routingKey,
                event
        );

        System.out.println("Mensaje enviado a RabbitMQ con routing key [" + routingKey + "]: " + event);
    }
}