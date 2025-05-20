package com.example.company.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PROJECT_QUEUE = "project.events.queue";
    public static final String TO_COMPANY_QUEUE = "coordinator.to.company.queue";
    public static final String LOGIN_TO_COMPANY_QUEUE = "company.queue"; // Nueva cola para login
    public static final String EXCHANGE = "company.exchange";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue projectQueue() {
        return new Queue(PROJECT_QUEUE, true);
    }

    @Bean
    public Queue toCompanyQueue() {
        return new Queue(TO_COMPANY_QUEUE, true);
    }

    @Bean
    public Queue loginToCompanyQueue() {
        return new Queue(LOGIN_TO_COMPANY_QUEUE, true); // La cola que escucha el listener
    }

    @Bean
    public Binding bindingProject(Queue projectQueue, DirectExchange exchange) {
        return BindingBuilder.bind(projectQueue).to(exchange).with("company.routingkey");
    }

    @Bean
    public Binding bindingToCompany(Queue toCompanyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(toCompanyQueue).to(exchange).with("coordinator.company.routingkey");
    }

    @Bean
    public Binding bindingLoginToCompany(Queue loginToCompanyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(loginToCompanyQueue).to(exchange).with("company.routingkey");
    }
}
