package com.example.login.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("COMPANY") 
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "companies")
public class Company extends User {
    
    @Column(name = "nit")
    private String nit;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "sector")
    private String sector;
    
    @Column(name = "contact_phone")
    private String contactPhone;
    
    @Column(name = "contact_first_name")
    private String contactFirstName;
    
    @Column(name = "contact_last_name")
    private String contactLastName;
    
    @Column(name = "contact_position")
    private String contactPosition;
    
    // Constructors, getters, and setters
    
    public Company() {
        super();
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
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getContactFirstName() {
        return contactFirstName;
    }
    
    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }
    
    public String getContactLastName() {
        return contactLastName;
    }
    
    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }
    
    public String getContactPosition() {
        return contactPosition;
    }
    
    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }
}