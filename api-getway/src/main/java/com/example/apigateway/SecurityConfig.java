package com.example.apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        // Rutas restringidas por rol
                        .pathMatchers("/students/**").hasRole("student")
                        .pathMatchers("/projects/**").hasRole("company")
                        .pathMatchers("/coordinators/**").hasRole("coordinator")

                        // Cualquier otra ruta requiere autenticación
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        return http.build();
    }
}
