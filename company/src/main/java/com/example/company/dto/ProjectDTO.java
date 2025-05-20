package com.example.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private UUID id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private String companyName;
    private String companyNit;
    private String justification;
}
