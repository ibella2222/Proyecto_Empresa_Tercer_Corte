package com.example.student.Infrastructure.Config; // Usando el paquete de tu log

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // <-- Anotación #1 (para la clase)
public class RabbitMQConfig {

    // Constantes para evitar errores de tipeo
    public static final String EXCHANGE_FROM_COMPANY = "company.exchange";
    public static final String ROUTING_KEY_FROM_COMPANY = "company.project.routingkey";
    public static final String STUDENT_PROJECT_QUEUE = "projects.queue.for-student-service";

    // --- AÑADIR ESTAS CONSTANTES ---
    // Definen a dónde enviaremos los mensajes de estudiantes
    public static final String STUDENT_EXCHANGE = "student.exchange";
    public static final String STUDENT_ROUTING_KEY = "student.routingkey";
    // --- AÑADIR ESTE BEAN ---
    /**
     * Declara el exchange al que este servicio publicará los eventos de estudiantes.
     * No es estrictamente necesario para enviar, pero es una buena práctica para la consistencia.
     */
    @Bean
    public DirectExchange studentExchange() {
        return new DirectExchange(STUDENT_EXCHANGE);
    }

    /**
     * Este @Bean crea el Exchange. Spring lo guarda.
     */
    @Bean // <-- Anotación #2 (para el Exchange)
    public DirectExchange companyExchange() {
        return new DirectExchange(EXCHANGE_FROM_COMPANY);
    }

    /**
     * ✅ ESTE ES EL BEAN QUE FALTA ✅
     * Este @Bean crea la Queue. Spring la guarda.
     * Sin la anotación @Bean, este método es solo un método normal y Spring lo ignora.
     */
    @Bean // <-- Anotación #3 (para la Queue)
    public Queue studentProjectQueue() {
        return new Queue(STUDENT_PROJECT_QUEUE, true);
    }

    /**
     * Cuando Spring intenta crear este @Bean, busca los dos beans que necesita como parámetros.
     * Si no encuentra el @Bean de la cola, falla.
     */
    @Bean // <-- Anotación #4 (para el Binding)
    public Binding binding(Queue studentProjectQueue, DirectExchange companyExchange) {
        return BindingBuilder
                .bind(studentProjectQueue)
                .to(companyExchange)
                .with(ROUTING_KEY_FROM_COMPANY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Creamos un configurador de JSON (ObjectMapper)
        ObjectMapper objectMapper = new ObjectMapper();

        // Aquí está la magia: le añadimos el "manual de instrucciones" para fechas
        objectMapper.registerModule(new JavaTimeModule());

        // Creamos el convertidor de mensajes de Spring usando nuestro ObjectMapper configurado
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}