package com.example.login.DTO;

import java.io.Serializable;

public class CompanyDTO implements Serializable {
    
    private int id;
    private String username;
    private String nit;
    private String name;
    private String sector;
    private String contactPhone;
    private String contactFirstName;
    private String contactLastName;
    private String contactPosition;
    
    // Default constructor needed for serialization
    public CompanyDTO() {
    }
    
    public CompanyDTO(int id, String username, String nit, String name, String sector, 
                     String contactPhone, String contactFirstName, String contactLastName, 
                     String contactPosition) {
        this.id = id;
        this.username = username;
        this.nit = nit;
        this.name = name;
        this.sector = sector;
        this.contactPhone = contactPhone;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactPosition = contactPosition;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    @Override
    public String toString() {
        return "CompanyDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nit='" + nit + '\'' +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactFirstName='" + contactFirstName + '\'' +
                ", contactLastName='" + contactLastName + '\'' +
                ", contactPosition='" + contactPosition + '\'' +
                '}';
    }
}