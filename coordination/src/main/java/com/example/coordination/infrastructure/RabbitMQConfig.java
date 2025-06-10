package com.example.coordination.infrastructure;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    private final ObjectMapper objectMapper;

    public RabbitMQConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // --- Exchanges ---
    public static final String COMPANY_EXCHANGE = "company.exchange";
    public static final String STUDENT_EXCHANGE = "student.exchange"; // <-- NUEVO

    // --- Colas ---
    public static final String PROJECT_QUEUE_FROM_COMPANY = "company.project.queue";
    public static final String STUDENT_QUEUE_FOR_COORDINATOR = "student.queue"; // <-- La que usa tu Listener

    // --- Routing Keys ---
    public static final String PROJECT_ROUTING_KEY = "company.project.routingkey";
    public static final String STUDENT_ROUTING_KEY = "student.routingkey"; // <-- NUEVO
    // Clave para los mensajes que el Coordinador ENVÍA a la Compañía.

    public static final String COORDINATOR_ROUTING_KEY = "coordinator.company.routingkey";


    // --- Beans para la comunicación con COMPANY (ya los tenías) ---
    @Bean
    public DirectExchange companyExchange() {
        return new DirectExchange(COMPANY_EXCHANGE);
    }

    @Bean
    public Queue projectQueueFromCompany() {
        return new Queue(PROJECT_QUEUE_FROM_COMPANY, true);
    }

    @Bean
    public Binding companyProjectBinding() {
        return BindingBuilder
                .bind(projectQueueFromCompany())
                .to(companyExchange())
                .with(PROJECT_ROUTING_KEY);
    }

    // --- INICIO DE LA CONFIGURACIÓN FALTANTE PARA ESTUDIANTES ---

    /**
     * Declara el exchange para los eventos de estudiantes.
     */
    @Bean
    public DirectExchange studentExchange() {
        return new DirectExchange(STUDENT_EXCHANGE);
    }

    /**
     * Declara la cola que el StudentListener está escuchando.
     */
    @Bean
    public Queue studentQueueForCoordinator() {
        return new Queue(STUDENT_QUEUE_FOR_COORDINATOR, true);
    }

    /**
     * Enlaza la cola de estudiantes con el exchange de estudiantes usando un routing key.
     */
    @Bean
    public Binding studentBinding() {
        return BindingBuilder
                .bind(studentQueueForCoordinator())
                .to(studentExchange())
                .with(STUDENT_ROUTING_KEY);
    }

    // --- FIN DE LA CONFIGURACIÓN FALTANTE ---


    // --- Beans de configuración de mensajes (ya los tenías) ---
    @Bean
    public MessageConverter jsonMessageConverter() {
        // Usa el ObjectMapper personalizado de tu JacksonConfig para manejar fechas, etc.
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}