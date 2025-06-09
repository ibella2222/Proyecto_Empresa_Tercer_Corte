package com.example.student.Infrastructure.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita el uso de @PreAuthorize en los controladores
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF, ya que usamos JWT y no sesiones.
                .csrf(csrf -> csrf.disable())

                // 2. Configurar la gestión de sesiones como STATELESS.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configurar el servidor para que sea un 'Resource Server' de OAuth2 que valide JWTs.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))

                // 4. Definir reglas de autorización para las peticiones.
                .authorizeHttpRequests(authz -> authz
                        // Por ahora, para simplificar, exigimos que cualquier petición esté autenticada.
                        // Las reglas específicas ya las tienes en el controlador con @PreAuthorize.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
