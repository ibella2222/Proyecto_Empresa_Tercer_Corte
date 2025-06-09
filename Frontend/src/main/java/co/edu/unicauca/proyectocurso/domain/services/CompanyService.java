package co.edu.unicauca.proyectocurso.domain.services;

import co.edu.unicauca.proyectocurso.access.ICompanyRepository;
import co.edu.unicauca.proyectocurso.domain.entities.Company;
import java.util.List;

public class CompanyService {

    private final ICompanyRepository repository;

    public CompanyService(ICompanyRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra una nueva empresa usando parámetros individuales.
     */
    public boolean registerCompany(String username, String nit,
                                   String name, String sector, String contactPhone,
                                   String contactFirstName, String contactLastName,
                                   String contactPosition) {

        if (username == null || username.isBlank()) {
            return false;
        }

        Company company = new Company(username, nit, name, sector,
                                      contactPhone, contactFirstName, contactLastName,
                                      contactPosition);
        return repository.save(company, username);
    }

    /**
     * Registra una nueva empresa usando un objeto Company.
     */
    public boolean registerCompany(Company company, String username) {
        if (company == null || username == null || company.getUsername().isBlank()) {
            return false;
        }
        return repository.save(company, username);
    }

    /**
     * Retorna todas las empresas registradas
     */
    public List<Company> listCompanies() {
        return repository.findAll();
    }

    /**
     * Busca una empresa por su NIT dentro de una lista específica
     */
    public Company findCompanyByNit(List<Company> companies, String nit) {
        return companies.stream()
                .filter(company -> company.getNit().equals(nit))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca una empresa por NIT usando el repositorio
     */
    public Company findCompanyByNit(String nit) {
        List<Company> companies = repository.findAll();
        return findCompanyByNit(companies, nit);
    }

    /**
     * Verifica si un NIT ya está registrado
     */
    public boolean existsCompanyNIT(String nit) {
        return repository.existsCompanyNIT(nit);
    }
}
