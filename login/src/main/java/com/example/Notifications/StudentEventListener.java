package com.example.Notifications;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class StudentEventListener {

        @RabbitListener(queues = "student.queue")
    public void handleUserEvent(String message) {
        System.out.println("Received message: " + message);
        // Procesar el mensaje según se requiera
    }

}
