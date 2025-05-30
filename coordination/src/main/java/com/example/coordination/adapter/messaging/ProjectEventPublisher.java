package com.example.coordination.adapter.messaging;

import com.example.coordination.domain.model.ProjectStateEnum;
import com.example.coordination.dto.ProjectDTO;
import com.example.coordination.infrastructure.RabbitMQConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
        ProjectDTO event = new ProjectDTO(
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

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            String json = mapper.writeValueAsString(event);

            String routingKey = RabbitMQConfig.COORDINATOR_ROUTING_KEY;

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.COMPANY_EXCHANGE,
                    routingKey,
                    json
            );

            System.out.println("📤 Proyecto enviado como JSON a RabbitMQ:");
            System.out.println(json);

        } catch (Exception e) {
            System.err.println("❌ Error serializando ProjectDTO a JSON: " + e.getMessage());
        }
    }
}