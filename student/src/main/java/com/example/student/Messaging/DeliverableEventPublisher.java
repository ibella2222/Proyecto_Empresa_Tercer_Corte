
package com.example.student.Messaging;

import com.example.student.Entities.Deliverable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.example.student.Config.RabbitMQDeliverableConfig.EXCHANGE;
import static com.example.student.Config.RabbitMQDeliverableConfig.ROUTING_KEY;

@Component
public class DeliverableEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public DeliverableEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDeliverable(Deliverable deliverable) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", deliverable.getId());
        payload.put("title", deliverable.getTitle());
        payload.put("studentId", deliverable.getStudentId());
        payload.put("projectId", deliverable.getProjectId());
        payload.put("submittedDate", deliverable.getSubmittedDate());
        payload.put("completed", deliverable.isCompleted());
        payload.put("comments", deliverable.getComments());
        payload.put("fileUrl", deliverable.getFileUrl());

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, payload);
        System.out.println("✅ Evento de entregable enviado: " + payload);
    }
}
