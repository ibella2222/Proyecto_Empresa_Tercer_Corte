package com.example.student.Infrastructure.Config;

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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita el uso de @PreAuthorize en los controladores
public class SecurityConfig {

    // Define el ID del cliente de Keycloak desde donde se leerán los roles.
    // Basado en tu token anterior, el client-id es "desktop".
    private static final String KEYCLOAK_CLIENT_ID = "desktop";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF para APIs stateless.
                .csrf(csrf -> csrf.disable())

                // 2. Configurar la gestión de sesiones como STATELESS.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configurar la validación de tokens JWT y la extracción de roles.
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )

                // 4. Reglas de autorización: requerir autenticación para cualquier petición.
                // La autorización por rol específica se maneja con @PreAuthorize en el controlador.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Crea un conversor de JWT que utiliza nuestro método personalizado para extraer
     * las autoridades (roles) del token.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    /**
     * Extrae los roles del token desde la sección "resource_access".
     * Este método busca los roles asignados específicamente a nuestro cliente en Keycloak.
     *
     * @param jwt El token JWT decodificado.
     * @return Una colección de GrantedAuthority (roles) para Spring Security.
     */
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        // Busca el objeto "resource_access" que contiene los roles por cliente.
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(KEYCLOAK_CLIENT_ID)) {
            return Collections.emptyList();
        }

        // Obtiene el acceso específico de nuestro cliente.
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_CLIENT_ID);
        if (clientAccess == null || !clientAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        // Obtiene la lista de roles.
        List<String> roles = (List<String>) clientAccess.get("roles");

        // Convierte la lista de strings de roles al formato que Spring Security necesita,
        // añadiendo el prefijo "ROLE_".
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}