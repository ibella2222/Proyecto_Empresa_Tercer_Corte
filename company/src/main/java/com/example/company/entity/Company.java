package com.example.company.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Company {
    
    @Column(unique = true, nullable = false)
    @Id
    private String username;
    private String nit;
    private String name;
    private String sector;

    public Company(String username, String nit, String name, String sector, String contactFirstName, String contactLastName, String contactPhone, String contactPosition) {
        this.username = username;
        this.nit = nit;
        this.name = name;
        this.sector = sector;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactPosition = contactPosition;
    }

    public Company() {

    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    private String contactFirstName;
    private String contactLastName;
    private String contactPhone;
    private String contactPosition;
}
