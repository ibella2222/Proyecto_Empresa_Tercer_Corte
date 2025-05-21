package com.example.company.Messaging;

import com.example.company.dto.CompanyDTO;
import com.example.company.service.CompanyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component

public class LoginListener {

    private final CompanyService companyService;

    public LoginListener(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RabbitListener(queues = "company.login.queue")
    public void receiveCompanyFromLogin(CompanyDTO dto) {
        System.out.println(" CompanyDTO recibido desde RabbitMQ (LOGIN):");
        System.out.println("NIT: " + dto.getNit());
        // Guardar en la base de datos
        companyService.saveCompanyDTO(dto);
    }
}

