package com.example.company.dto;


public class CompanyDTO {
    private String nit;
    private String name;
    private String sector;
    private String contactPhone;
    private String contactFirstName;
    private String contactLastName;
    private String contactPosition;

    public CompanyDTO() {}

    // Getters y Setters
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactFirstName() { return contactFirstName; }
    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }

    public String getContactLastName() { return contactLastName; }
    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }

    public String getContactPosition() { return contactPosition; }
    public void setContactPosition(String contactPosition) { this.contactPosition = contactPosition; }
}
