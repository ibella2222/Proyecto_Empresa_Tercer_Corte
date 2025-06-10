package com.example.apigateway;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // Inyectamos el valor del issuer-uri desde application.yml
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    /**
     * Esta es la configuración principal de la cadena de filtros de seguridad.
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/students/**").hasRole("student")
                        .pathMatchers("/projects/**").hasRole("company")
                        .pathMatchers("/coordinators/**").hasRole("coordinator")
                        .pathMatchers("/companies/**").authenticated()
                        .anyExchange().authenticated()
                )
                // Le decimos que use la configuración por defecto para JWT,
                // la cual automáticamente usará el Bean 'jwtDecoder' que definimos abajo.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }

    /**
     * ESTE ES EL CAMBIO CLAVE:
     * Creamos manualmente el Bean que decodifica y valida los tokens JWT.
     * Al crear este Bean explícitamente, eliminamos cualquier ambigüedad
     * en la auto-configuración de Spring.
     * @return Un decodificador de JWT configurado para usar nuestro issuer de Keycloak.
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        // Usa el método de fábrica de Spring para crear un decodificador
        // a partir de la ubicación del emisor (issuer) de OpenID Connect.
        // Esto se encargará de descubrir el JWKS URI y validar los tokens.
        return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
    }
}