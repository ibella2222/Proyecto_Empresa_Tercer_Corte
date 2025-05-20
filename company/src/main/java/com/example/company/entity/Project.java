package com.example.company.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // UUID generado automáticamente

    private String name;
    private String summary;
    private String description;
    private String date; // Si prefieres usar tipo LocalDate, puedes cambiarlo
    private String state;
    private float budget;
    private int maxMonths;
    private String objectives;
    private String companyNIT;
    private String justification;
    private String companyName;
}
