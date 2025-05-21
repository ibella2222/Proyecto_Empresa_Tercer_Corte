package com.example.company.Messaging;
import com.example.company.dto.CompanyDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LoginListener {

    private final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy") //  Formato correcto de fechas
            .create();

    // Ahora escucha la cola específica para login
    @RabbitListener(queues = "company.login.queue")
    public void receiveCompanyFromLogin(String message) {
        System.out.println(" Mensaje JSON recibido desde RabbitMQ (LOGIN):");
        System.out.println(message);

        try {
            CompanyDTO dto = gson.fromJson(message, CompanyDTO.class);
            System.out.println("➡ NIT: " + dto.getNit());
            // ...
        } catch (Exception e) {
            System.err.println(" Error al convertir JSON a CompanyDTO: " + e.getMessage());
        }
    }

}

