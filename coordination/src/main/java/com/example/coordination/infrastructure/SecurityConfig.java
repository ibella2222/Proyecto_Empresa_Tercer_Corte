package com.example.coordination.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuración de seguridad para el microservicio de Coordinador.
 * Habilita la validación de tokens JWT y la seguridad a nivel de métodos.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Esencial para que @PreAuthorize("hasRole('coordinator')") funcione
public class SecurityConfig {

    // El ID del cliente en Keycloak del cual extraeremos los roles.
    private static final String KEYCLOAK_CLIENT_ID = "desktop";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF: No es necesario para APIs stateless.
                .csrf(csrf -> csrf.disable())

                // 2. No crear sesiones HTTP: Cada petición debe ser autenticada por sí misma.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configurar como Servidor de Recursos OAuth2 y usar nuestro conversor de JWT.
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )

                // 4. Regla general: cualquier petición debe estar autenticada.
                // La autorización específica se maneja en los controladores con @PreAuthorize.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Crea el conversor que le enseña a Spring Security a extraer los roles
     * de la sección 'resource_access' del token.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    /**
     * Lógica para extraer los roles de un cliente específico ("desktop") dentro del token JWT
     * y prepararlos para que Spring Security los entienda (añadiendo el prefijo "ROLE_").
     */
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(KEYCLOAK_CLIENT_ID)) {
            return Collections.emptyList();
        }

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_CLIENT_ID);
        if (clientAccess == null || !clientAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) clientAccess.get("roles");

        return roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}