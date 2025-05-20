package com.example.student.Messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProjectEventListener {

    @RabbitListener(queues = "student.projectstate.queue")
    public void onProjectStateChanged(Map<String, Object> message) {
        if ("ProjectStateChanged".equals(message.get("type"))) {
            System.out.println("Estado del proyecto actualizado para el estudiante:");
            System.out.println(message);
        }
    }
}