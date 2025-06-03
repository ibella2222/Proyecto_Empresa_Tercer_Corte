package com.example.coordination.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "project_stats")
@Data
@NoArgsConstructor
public class ProjectStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID projectId;

    @Enumerated(EnumType.STRING)
    private ProjectStateEnum state;

    private LocalDate changeDate;

}