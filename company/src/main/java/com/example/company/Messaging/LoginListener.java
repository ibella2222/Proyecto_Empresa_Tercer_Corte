package com.example.company.Messaging;
import com.example.company.dto.CompanyDTO;
import com.example.company.entity.Company;
import com.example.company.repository.CompanyRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LoginListener {

    private final CompanyRepository companyRepository;

    public LoginListener(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @RabbitListener(queues = "company.queue")
    public void receiveCompanyFromLogin(CompanyDTO dto) {
        Company company = new Company();
        company.setNit(dto.getNit());
        company.setName(dto.getName());
        company.setSector(dto.getSector());
        company.setContactFirstName(dto.getContactFirstName());
        company.setContactLastName(dto.getContactLastName());
        company.setContactPhone(dto.getContactPhone());
        company.setContactPosition(dto.getContactPosition());

        companyRepository.save(company);
        System.out.println("Empresa registrada desde login: " + company.getName());
    }
}