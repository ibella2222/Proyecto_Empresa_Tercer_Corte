
package com.example.student.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDeliverableConfig {

    public static final String QUEUE = "deliverables.queue";
    public static final String EXCHANGE = "deliverables.exchange";
    public static final String ROUTING_KEY = "deliverables.routingkey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
