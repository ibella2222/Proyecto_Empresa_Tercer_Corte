package com.example.Notifications;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    @RabbitListener(queues = "user.queue")
    public void handleUserEvent(String message) {
        System.out.println("Received message: " + message);
        // Procesar el mensaje según se requiera
    }
}

