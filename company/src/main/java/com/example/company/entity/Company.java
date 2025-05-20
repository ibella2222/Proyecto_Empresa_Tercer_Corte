package com.example.company.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Company {
    @Id
    private String nit;

    private String name;
    private String sector;
    private String contactFirstName;
    private String contactLastName;
    private String contactPhone;
    private String contactPosition;
}
