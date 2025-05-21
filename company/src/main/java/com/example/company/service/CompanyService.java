package com.example.company.service;

import com.example.company.dto.CompanyDTO;
import com.example.company.entity.Company;
import com.example.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void saveCompanyDTO(CompanyDTO dto) {
        if (companyRepository.existsById(dto.getNit())) {
            System.out.println("⚠ Empresa con NIT " + dto.getNit() + " ya existe. No se guardará nuevamente.");
            return;
        }

        Company company = new Company();
        company.setNit(dto.getNit());
        company.setName(dto.getName());
        company.setSector(dto.getSector());
        company.setContactPhone(dto.getContactPhone());
        company.setContactFirstName(dto.getContactFirstName());
        company.setContactLastName(dto.getContactLastName());
        company.setContactPosition(dto.getContactPosition());

        companyRepository.save(company);
        System.out.println(" Empresa guardada en la base de datos con NIT: " + dto.getNit());
    }
}
