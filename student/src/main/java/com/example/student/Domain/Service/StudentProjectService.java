package com.example.student.Domain.Service;


import com.example.student.Infrastructure.Dto.ProjectDTO;
import com.example.student.Domain.Model.StudentProject;
import com.example.student.Repository.StudentProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentProjectService {

    private final StudentProjectRepository studentProjectRepository;

    @Autowired
    public StudentProjectService(StudentProjectRepository studentProjectRepository) {
        this.studentProjectRepository = studentProjectRepository;
    }

    @Transactional
    public void saveNewAvailableProject(ProjectDTO dto) {
        // --- CHECKPOINT 2: ¿SE EJECUTA EL MÉTODO DEL SERVICIO? ---
        System.out.println("--- [DEBUG 2: SERVICE] ---");
        System.out.println("Método saveNewAvailableProject ejecutado.");

        // --- CHECKPOINT 3: ¿LA CONVERSIÓN DE DTO A ENTIDAD FUNCIONA? ---
        StudentProject projectEntity = new StudentProject();
        projectEntity.setId(dto.getId());
        projectEntity.setName(dto.getName());
        projectEntity.setSummary(dto.getSummary());
        projectEntity.setObjectives(dto.getObjectives());
        projectEntity.setDescription(dto.getDescription());
        projectEntity.setDate(dto.getDate());
        projectEntity.setFinalizationDate(dto.getFinalizationDate());
        projectEntity.setCompanyNIT(dto.getCompanyNIT());
        projectEntity.setJustification(dto.getJustification());
        projectEntity.setBudget(dto.getBudget());
        projectEntity.setMaxMonths(dto.getMaxMonths());
        projectEntity.setState("AVAILABLE");

        System.out.println("--- [DEBUG 3: SERVICE] ---");
        System.out.println("Mapeo de DTO a Entidad completado.");
        System.out.println("DATOS A GUARDAR (ENTIDAD): " + projectEntity.toString());

        // --- CHECKPOINT 4: ¿SE LLAMA AL MÉTODO SAVE? ---
        System.out.println("--- [DEBUG 4: SERVICE] ---");
        System.out.println("Intentando guardar la entidad en la base de datos...");

        studentProjectRepository.save(projectEntity);

        // --- CHECKPOINT 5: ¿LA OPERACIÓN DE GUARDADO TERMINA? ---
        System.out.println("--- [DEBUG 5: SERVICE] ---");
        System.out.println("✅ ¡Comando save() ejecutado sin errores aparentes!");
    }
}