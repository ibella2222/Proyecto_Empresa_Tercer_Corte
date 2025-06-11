package co.edu.unicauca.proyectocurso.access;

import co.edu.unicauca.proyectocurso.domain.entities.ApplicationDTO;
import co.edu.unicauca.proyectocurso.domain.entities.StudentProject;
import java.util.List;

public interface ICoordinatorRepository {
    List<ApplicationDTO> getPendingApplications();
    boolean approveStudentApplication(String studentId, String projectId);
    boolean rejectStudentApplication(String studentId, String projectId);
    boolean approveProject(String projectId);

}