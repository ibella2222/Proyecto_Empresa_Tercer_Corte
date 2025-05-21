package com.example.company.config;

import com.example.company.entity.Company;
import com.example.company.repository.CompanyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CompanyRepository companyRepository;

    public DataLoader(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostConstruct
    public void init() {
        if (companyRepository.count() == 0) {
            Company c1 = new Company();
            c1.setNit("1");
            c1.setName("Empresa Demo 1");
            c1.setSector("Tecnología");
            c1.setContactPhone("123456789");
            c1.setContactFirstName("Juan");
            c1.setContactLastName("Pérez");
            c1.setContactPosition("Gerente");

            Company c2 = new Company();
            c2.setNit("222");
            c2.setName("Empresa Demo 2");
            c2.setSector("Salud");
            c2.setContactPhone("987654321");
            c2.setContactFirstName("Ana");
            c2.setContactLastName("Gómez");
            c2.setContactPosition("Directora");

            companyRepository.save(c1);
            companyRepository.save(c2);

            System.out.println("📦 Empresas precargadas en la base de datos.");
        }
    }
}
