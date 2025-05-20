package com.example.student.Service;

import com.example.student.Entities.Project;
import com.example.student.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project findById(UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findByName(String name) {
        return projectRepository.findByName(name);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }

}
