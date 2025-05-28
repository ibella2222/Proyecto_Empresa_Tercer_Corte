package com.example.coordination.infrastructure;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange usado para comunicarse con el microservicio Company
    public static final String COMPANY_EXCHANGE = "company.exchange";

    // Colas necesarias para coordinación
    public static final String PROJECT_QUEUE = "company.project.queue";  // ← Recibe de company
    public static final String COORDINATOR_TO_COMPANY_QUEUE = "coordinator.to.company.queue"; // ← Enviada por coordinación

    // Claves de enrutamiento
    public static final String PROJECT_ROUTING_KEY = "company.project.routingkey";
    public static final String COORDINATOR_ROUTING_KEY = "coordinator.company.routingkey";

    @Bean
    public DirectExchange companyExchange() {
        return new DirectExchange(COMPANY_EXCHANGE);
    }

    // ⬇️ Cola para recibir mensajes de Company
    @Bean
    public Queue companyProjectQueue() {
        return new Queue(PROJECT_QUEUE, true);
    }

    @Bean
    public Binding companyProjectBinding() {
        return BindingBuilder
                .bind(companyProjectQueue())
                .to(companyExchange())
                .with(PROJECT_ROUTING_KEY);
    }

    // ⬇️ Cola que Coordination publicará para que la consuma Company
    @Bean
    public Queue coordinatorToCompanyQueue() {
        return new Queue(COORDINATOR_TO_COMPANY_QUEUE, true);
    }

    @Bean
    public Binding coordinatorToCompanyBinding() {
        return BindingBuilder
                .bind(coordinatorToCompanyQueue())
                .to(companyExchange())
                .with(COORDINATOR_ROUTING_KEY);
    }
}