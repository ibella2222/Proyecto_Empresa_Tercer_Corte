package com.example.coordination.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange tipo topic
    public static final String PROJECT_EVENT_EXCHANGE = "project.events.exchange";

    // Cola unificada
    public static final String PROJECT_EVENTS_QUEUE = "project.events.queue";

    public static final String PROJECT_EVENTS_CRISTOBAL_QUEUE = "project.events.cristobal.queue";

    // Routing key comodín para todos los eventos
    public static final String PROJECT_EVENTS_ROUTING_KEY = "project.*";

    @Bean
    public Queue coordinatorMessagesQueue() {
        return new Queue("coordinator.messages.queue", true);
    }
    @Bean
    public TopicExchange projectExchange() {
        return new TopicExchange(PROJECT_EVENT_EXCHANGE);
    }

    @Bean
    public Queue projectEventsQueue() {
        return new Queue(PROJECT_EVENTS_QUEUE);
    }

    @Bean
    public Binding projectEventsBinding() {
        return BindingBuilder
                .bind(projectEventsQueue())
                .to(projectExchange())
                .with(PROJECT_EVENTS_ROUTING_KEY);
    }

    @Bean
    public Queue projectEventsCristobalQueue() {
        return new Queue(PROJECT_EVENTS_CRISTOBAL_QUEUE);
    }

    @Bean
    public Binding projectEventsCristobalBinding() {
        return BindingBuilder
                .bind(projectEventsCristobalQueue())
                .to(projectExchange())
                .with(PROJECT_EVENTS_ROUTING_KEY);  // o "project.approved" si es más específico
    }

    @Bean
    public Queue studentQueue() {
        return new Queue("student.queue");
    }

}
