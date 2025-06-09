package com.example.student.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private String program;
    private String projectId;

    @Override
    public String toString() {
        return "StudentDTO{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", program='" + program + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
