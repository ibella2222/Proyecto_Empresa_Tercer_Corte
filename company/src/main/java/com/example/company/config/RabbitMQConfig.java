package com.example.company.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de colas
    public static final String PROJECT_QUEUE = "company.project.queue";
    public static final String LOGIN_QUEUE = "company.login.queue";
    public static final String COORDINATOR_TO_COMPANY_QUEUE = "coordinator.to.company.queue";

    // Nombre del exchange
    public static final String EXCHANGE = "company.exchange";

    // Claves de enrutamiento
    public static final String PROJECT_ROUTING_KEY = "company.project.routingkey";
    public static final String LOGIN_ROUTING_KEY = "company.login.routingkey";
    public static final String COORDINATOR_ROUTING_KEY = "coordinator.company.routingkey";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue projectQueue() {
        return new Queue(PROJECT_QUEUE, true);
    }

    @Bean
    public Queue loginQueue() {
        return new Queue(LOGIN_QUEUE, true);
    }

    @Bean
    public Queue coordinatorToCompanyQueue() {
        return new Queue(COORDINATOR_TO_COMPANY_QUEUE, true);
    }

    @Bean
    public Binding projectBinding() {
        return BindingBuilder.bind(projectQueue()).to(exchange()).with(PROJECT_ROUTING_KEY);
    }

    @Bean
    public Binding loginBinding() {
        return BindingBuilder.bind(loginQueue()).to(exchange()).with(LOGIN_ROUTING_KEY);
    }

    @Bean
    public Binding coordinatorBinding() {
        return BindingBuilder.bind(coordinatorToCompanyQueue()).to(exchange()).with(COORDINATOR_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
