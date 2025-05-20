package com.example.login.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {

    public static final String USER_QUEUE = "user.queue";
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_ROUTING_KEY = "user.routingkey";

    // Renombramos el método a userQueue (minúscula inicial) y lo marcamos como @Primary
    @Bean
    @Primary
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    // Método para la DirectExchange, renombrado a userExchange
    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(USER_EXCHANGE);
    }

    // Se inyecta explícitamente el bean userQueue usando @Qualifier
    @Bean
    public Binding userBinding(@Qualifier("userQueue") Queue userQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(USER_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // BEANS para empresas, configurados en otra clase (por ejemplo, RabbitMQCompanyConfig)
}
