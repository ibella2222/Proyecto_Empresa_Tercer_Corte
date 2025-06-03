package com.example.coordination.domain.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.coordination.adapter.repository.ProjectStatsRepository;
import com.example.coordination.domain.model.ProjectStats;

@Service
public class ProjectStatsService {
    private final ProjectStatsRepository repository;

    public ProjectStatsService(ProjectStatsRepository repository) {
        this.repository = repository;

    }

    public List<ProjectStats> listAll() {
        return repository.findAll();
    }
}
