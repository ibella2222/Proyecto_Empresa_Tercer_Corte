package com.example.coordination.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.coordination.domain.model.ProjectStats;


public interface ProjectStatsRepository extends JpaRepository<ProjectStats, Long> {

}
