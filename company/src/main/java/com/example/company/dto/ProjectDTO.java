package com.example.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO implements Serializable {
    private UUID id;
    private String name;
    private String summary;
    private String objectives;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate finalizationDate;
    
    private String state;
    private String companyNIT;
    private String justification;
    private float budget;
    private int maxMonths;
}
