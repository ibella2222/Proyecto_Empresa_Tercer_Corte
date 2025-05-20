package com.example.student.Messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public StudentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishStudentApplied(String studentId, String projectId) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", "StudentAppliedToProject");
        event.put("studentId", studentId);
        event.put("projectId", projectId);

        rabbitTemplate.convertAndSend("coordinator.exchange", "coordinator.routingkey", event);
    }

    public void publishDeliverableSubmitted(String studentId, String deliverableId) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", "DeliverableSubmitted");
        event.put("studentId", studentId);
        event.put("deliverableId", deliverableId);

        rabbitTemplate.convertAndSend("coordinator.exchange", "coordinator.routingkey", event);
    }
}