package com.example.login.Config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCompanyConfig {

    public static final String COMPANY_QUEUE = "company.login.queue";           // 🟢 Cola correcta
    public static final String COMPANY_EXCHANGE = "company.exchange";           // 🟢 Mismo exchange
    public static final String COMPANY_ROUTING_KEY = "company.login.routingkey"; // 🟢 Routing key nueva

    @Bean
    public Queue companyQueue() {
        return new Queue(COMPANY_QUEUE, true);
    }

    @Bean
    public DirectExchange companyExchange() {
        return new DirectExchange(COMPANY_EXCHANGE);
    }

    @Bean
    public Binding companyBinding(Queue companyQueue, DirectExchange companyExchange) {
        return BindingBuilder.bind(companyQueue).to(companyExchange).with(COMPANY_ROUTING_KEY);
    }
}
