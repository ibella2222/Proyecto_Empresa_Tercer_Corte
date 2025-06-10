package com.example.company.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para la transferencia de datos de la Compañía.
 * Usa Lombok para generar automáticamente el código repetitivo (boilerplate).
 */
@Data // Genera getters, setters, toString, equals y hashCode.
@NoArgsConstructor // Genera un constructor vacío: public CompanyDTO() {}
@AllArgsConstructor // Genera un constructor con todos los argumentos.
public class CompanyDTO {

    // Solo necesitas declarar los campos. Lombok se encarga del resto.
    private String username;
    private String nit;
    private String name;
    private String sector;
    private String contactPhone;
    private String contactFirstName;
    private String contactLastName;
    private String contactPosition;

}