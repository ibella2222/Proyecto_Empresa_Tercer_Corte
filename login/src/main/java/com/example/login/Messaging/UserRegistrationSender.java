package com.example.login.Messaging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.login.Config.RabbitMQConfig;
import com.example.login.DTO.LoginResponse;

@Component
public class UserRegistrationSender {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.userRegistration}")
    private String userQueue;

    public UserRegistrationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserRegistration(LoginResponse response) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.USER_EXCHANGE,
            RabbitMQConfig.USER_ROUTING_KEY,
            response
        );
    }



}

