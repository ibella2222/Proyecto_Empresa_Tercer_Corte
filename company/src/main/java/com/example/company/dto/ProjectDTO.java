package com.example.company.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
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
    // Si vas a usarlo después
    private float budget;
    private int maxMonths;
}