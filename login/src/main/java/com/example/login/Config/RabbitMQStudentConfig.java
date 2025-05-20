package com.example.login.Config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQStudentConfig {

    public static final String STUDENT_QUEUE = "student.queue";
    public static final String STUDENT_EXCHANGE = "student.exchange";
    public static final String STUDENT_ROUTING_KEY = "student.routingkey";

    @Bean
    public Queue studentQueue() {
        return new Queue(STUDENT_QUEUE, true);
    }

    @Bean
    public DirectExchange studentExchange() {
        return new DirectExchange(STUDENT_EXCHANGE);
    }

    @Bean
    public Binding studentBinding(Queue studentQueue, DirectExchange studentExchange) {
        return BindingBuilder.bind(studentQueue).to(studentExchange).with(STUDENT_ROUTING_KEY);
    }
    
}

