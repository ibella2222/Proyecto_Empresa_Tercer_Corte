package com.example.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String summary;
    private String objectives;
    private String description;

    private LocalDate date;
    private Date finalizationDate;

    private String state;
    private String companyNIT;
    private String justification;
    // Opcionalmente puedes mantener estos si los quieres usar
    private float budget;
    private int maxMonths;
}
