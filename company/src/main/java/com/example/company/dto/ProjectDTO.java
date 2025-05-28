package com.example.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO implements Serializable {
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
