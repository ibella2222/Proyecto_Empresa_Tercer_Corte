package com.example.coordination.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(FORMATTER));

        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}