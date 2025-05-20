package com.example.student.Config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQStudentConfig {

    // Exchange principal
    public static final String STUDENT_EXCHANGE = "student.exchange";

    // Cola 1: para mensajes generales
    public static final String STUDENT_QUEUE = "student.queue";
    public static final String STUDENT_ROUTING_KEY = "student.routingkey";

    // Cola 2: para cambios de estado de proyectos
    public static final String STUDENT_PROJECT_STATE_QUEUE = "student.projectstate.queue";
    public static final String STUDENT_PROJECT_STATE_ROUTING_KEY = "student.projectstate.routingkey";

    @Bean
    public DirectExchange studentExchange() {
        return new DirectExchange(STUDENT_EXCHANGE);
    }

    @Bean
    public Queue studentQueue() {
        return new Queue(STUDENT_QUEUE, true);
    }

    @Bean
    public Binding studentBinding() {
        return BindingBuilder.bind(studentQueue())
                .to(studentExchange())
                .with(STUDENT_ROUTING_KEY);
    }

    @Bean
    public Queue projectStateQueue() {
        return new Queue(STUDENT_PROJECT_STATE_QUEUE, true);
    }

    @Bean
    public Binding projectStateBinding() {
        return BindingBuilder.bind(projectStateQueue())
                .to(studentExchange())
                .with(STUDENT_PROJECT_STATE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
