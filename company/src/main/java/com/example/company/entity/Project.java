package com.example.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String summary;
    private String objectives;
    private String description;

    private LocalDate date;
    private LocalDate finalizationDate;

    private String state;
    private String companyNIT;
    private String justification;

    private float budget;
    private int maxMonths;
}
