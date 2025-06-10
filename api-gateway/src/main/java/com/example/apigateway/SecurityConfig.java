package com.example.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${keycloak.client-id}") // Añade esta propiedad en tu application.yml
    private String keycloakClientId;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.GET, "/projects", "/projects/**").authenticated()
                        .pathMatchers("/projects/**").hasRole("company")
                        .pathMatchers("/students/**").hasRole("student")
                        .pathMatchers("/coordinators/**").hasRole("coordinator")
                        .pathMatchers("/companies/**").authenticated()
                        .anyExchange().authenticated()
                )
                // CAMBIO CLAVE: Le decimos al resource server que use nuestro conversor personalizado
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    /**
     * Crea el conversor que Spring Security usará para transformar un JWT en un objeto de Autenticación.
     * Este es el puente entre el conversor de roles y el stack reactivo de Spring.
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Le asignamos nuestro conversor de roles personalizado
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter(keycloakClientId));
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}

/**
 * Clase auxiliar para extraer los roles de la sección 'resource_access' del token JWT.
 * Es idéntica a la que usaríamos en un microservicio no reactivo.
 */
class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String keycloakClientId;

    public KeycloakRoleConverter(String keycloakClientId) {
        this.keycloakClientId = keycloakClientId;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || resourceAccess.isEmpty() || !resourceAccess.containsKey(keycloakClientId)) {
            return Collections.emptyList();
        }

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(keycloakClientId);
        if (clientAccess == null || !clientAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        Collection<String> roles = (Collection<String>) clientAccess.get("roles");
        return roles.stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}