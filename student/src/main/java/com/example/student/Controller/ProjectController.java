package com.example.student.Controller;

import com.example.student.DTO.ProjectDTO;
import com.example.student.Entities.Project;
import com.example.student.Mapper.ProjectMapper;
import com.example.student.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "*") // Permitir solicitudes de cualquier origen
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * Endpoint para obtener todos los proyectos
     * @return Lista de proyectos en formato DTO compatible con el frontend
     */
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDTO> projectDTOs = ProjectMapper.toDTOList(projects);
        return ResponseEntity.ok(projectDTOs);
    }

    /**
     * Endpoint para obtener un proyecto por su ID
     * @param id ID del proyecto a buscar
     * @return El proyecto en formato DTO compatible con el frontend
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable UUID id) {
        Project project = projectService.findById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProjectMapper.toDTO(project));
    }

    /**
     * Endpoint para buscar un proyecto por su nombre
     * @param name Nombre del proyecto a buscar
     * @return El proyecto en formato DTO compatible con el frontend
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable String name) {
        Project project = projectService.findByName(name);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProjectMapper.toDTO(project));
    }

    /**
     * Endpoint para crear un nuevo proyecto
     * @param projectDTO Datos del proyecto a crear en formato DTO
     * @return El proyecto creado en formato DTO compatible con el frontend
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        Project project = ProjectMapper.toEntity(projectDTO);
        Project savedProject = projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectMapper.toDTO(savedProject));
    }

    /**
     * Endpoint para actualizar un proyecto existente
     * @param id ID del proyecto a actualizar
     * @param projectDTO Datos actualizados del proyecto en formato DTO
     * @return El proyecto actualizado en formato DTO compatible con el frontend
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable UUID id, @RequestBody ProjectDTO projectDTO) {
        // Verificar que el proyecto existe
        Project existingProject = projectService.findById(id);
        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }

        // Asegurarse de que el ID no cambie
        projectDTO.setId(id);

        // Convertir DTO a entidad y guardar
        Project project = ProjectMapper.toEntity(projectDTO);
        Project updatedProject = projectService.saveProject(project);

        return ResponseEntity.ok(ProjectMapper.toDTO(updatedProject));
    }

    /**
     * Endpoint para eliminar un proyecto
     * @param id ID del proyecto a eliminar
     * @return Respuesta sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        // Verificar que el proyecto existe
        Project existingProject = projectService.findById(id);
        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }

        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
