package com.example.login.Factory;

import com.example.login.Entities.User;

public class UserFactory {
    public static User createUser(String username, String password, String roleStr) {
        // En este caso, se usa directamente la cadena roleStr sin parsearla a enum
        return new User();
    }
}
