package com.example.login.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactiva CSRF completamente
            .csrf(csrf -> csrf.disable())

            // Permite iframes desde el mismo origen (necesario para H2 Console)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

            .authorizeHttpRequests(auth -> auth
                // Permitir acceso libre a todas las rutas
                .anyRequest().permitAll()
            )

            // Desactiva login por formulario y HTTP Basic
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
