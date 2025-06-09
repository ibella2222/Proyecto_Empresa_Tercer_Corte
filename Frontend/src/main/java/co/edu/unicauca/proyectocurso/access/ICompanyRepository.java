package co.edu.unicauca.proyectocurso.access;
import co.edu.unicauca.proyectocurso.domain.entities.Company;
import java.util.List;

public interface ICompanyRepository {
    
    /**
     * Guarda una nueva empresa.
     * @param company Empresa a registrar
     * @return true si se guardó correctamente, false en caso contrario
     */
    boolean save(Company company);

    /**
     * Retorna todas las empresas registradas.
     * @return Lista de empresas
     */
    List<Company> findAll();

    /**
     * Verifica si una empresa existe según su NIT.
     * @param companyNIT NIT de la empresa
     * @return true si existe, false en caso contrario
     */
    boolean existsCompanyNIT(String companyNIT);

    /**
     * Busca una empresa por su NIT.
     * @param nit NIT de la empresa
     * @return Objeto Company si existe, null si no
     */
    Company findByNIT(String nit);
}
