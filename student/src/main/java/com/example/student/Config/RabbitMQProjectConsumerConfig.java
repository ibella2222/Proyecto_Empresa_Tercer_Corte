
package com.example.student.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProjectConsumerConfig {

    public static final String PROJECT_QUEUE = "project.events.queue";
    public static final String PROJECT_EXCHANGE = "company.exchange";
    public static final String PROJECT_ROUTING_KEY = "company.routingkey";

    @Bean
    public Queue projectQueue() {
        return new Queue(PROJECT_QUEUE, true);
    }

    @Bean
    public DirectExchange projectExchange() {
        return new DirectExchange(PROJECT_EXCHANGE);
    }

    @Bean
    public Binding projectBinding(Queue projectQueue, DirectExchange projectExchange) {
        return BindingBuilder.bind(projectQueue).to(projectExchange).with(PROJECT_ROUTING_KEY);
    }
}
