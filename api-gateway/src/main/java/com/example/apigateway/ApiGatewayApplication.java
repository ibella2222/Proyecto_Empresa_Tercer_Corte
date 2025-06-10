package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);


    }

    // --- AÑADE ESTE MÉTODO BEAN ---
    @Bean
    public CommandLineRunner connectivityTest() {
        return args -> {
            System.out.println("--- INICIANDO TEST DE CONECTIVIDAD CON KEYCLOAK ---");
            try {
                // Esta es la URL de configuración que Spring Security intenta usar internamente
                String keycloakDiscoveryUrl = "http://localhost:8080/realms/sistema/.well-known/openid-configuration";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(keycloakDiscoveryUrl))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    System.out.println(">>> CONEXIÓN EXITOSA! El API Gateway PUEDE ver a Keycloak. El problema es otro.");
                    System.out.println("Respuesta de Keycloak: " + response.body().substring(0, 100) + "...");
                } else {
                    System.err.println(">>> ERROR DE CONEXIÓN! Código de respuesta: " + response.statusCode());
                }

            } catch (Exception e) {
                System.err.println(">>> ERROR DE RED FATAL! El API Gateway NO PUEDE conectarse a Keycloak.");
                e.printStackTrace();
            }
            System.out.println("--- FIN DEL TEST DE CONECTIVIDAD ---");
        };
    }

}
