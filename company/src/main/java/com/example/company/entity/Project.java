package com.example.company.entity;

import jakarta.persistence.*;
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
    @GeneratedValue
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
