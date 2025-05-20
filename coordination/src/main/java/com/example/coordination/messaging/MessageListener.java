package com.example.coordination.messaging;

import com.example.coordination.entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues = "coordinator.messages.queue")
    public void receiveMessage(Message message) {
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            System.out.println("Mensaje inválido recibido. Debe contener contenido.");
            return;
        }

        System.out.println("Mensaje recibido de " + message.getSender() + ": " + message.getContent());
    }
}