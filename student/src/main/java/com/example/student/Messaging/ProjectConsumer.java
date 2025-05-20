
package com.example.student.Messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectConsumer {

    @RabbitListener(queues = "project.events.queue")
    public void receiveProject(String message) {
        System.out.println("📦 Proyecto recibido por cola desde company: " + message);
    }
}
