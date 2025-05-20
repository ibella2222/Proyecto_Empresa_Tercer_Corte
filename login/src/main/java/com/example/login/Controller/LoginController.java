package com.example.login.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.login.DTO.LoginRequest;
import com.example.login.DTO.LoginResponse;
import com.example.login.Entities.User;
import com.example.login.Service.UserService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;
    
    /**
     * Endpoint para autenticar usuarios
     * @param request contiene username y password
     * @return ResponseEntity con datos del usuario y mensaje de éxito o error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> optionalUser = userService.findByUsername(request.getUsername());
            
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
            
            User user = optionalUser.get();
            
            if (!userService.validatePassword(user, request.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Contraseña incorrecta"));
            }
            
            // Crear respuesta sin JWT
            LoginResponse response = new LoginResponse();
            response.setMessage("Login exitoso");
            response.setUsername(user.getUsername());
            response.setUserId(user.getId());
            response.setRole(user.getRole());
            response.setProfileCompleted(user.isProfileCompleted());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error durante la autenticación: " + e.getMessage()));
        }
    }
    
    // En LoginController.java
@GetMapping("/user/role")
public ResponseEntity<?> getUserRole(
    @RequestParam String username,
    @RequestParam String password) {

    Optional<User> optionalUser = userService.findByUsername(username);
    
    if (optionalUser.isEmpty() || 
        !userService.validatePassword(optionalUser.get(), password)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales inválidas"));
    }

    return ResponseEntity.ok(Map.of("role", optionalUser.get().getRole()));
}
@GetMapping("/user/exists")
public ResponseEntity<?> userExists(@RequestParam String username) {
    boolean exists = userService.existsByUsername(username);
    return ResponseEntity.ok(Map.of("exists", exists));
}
@GetMapping("/user/{username}/profileCompleted")
public ResponseEntity<?> isProfileCompleted(@PathVariable String username) {
    Optional<User> optionalUser = userService.findByUsername(username);
    if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuario no encontrado"));
    }
    return ResponseEntity.ok(Map.of("profileCompleted", optionalUser.get().isProfileCompleted()));
}
@PutMapping("/user/{username}/profileCompleted")
public ResponseEntity<?> updateProfileCompleted(
        @PathVariable String username,
        @RequestBody Map<String, Boolean> body) {

    boolean completed = body.getOrDefault("profileCompleted", false);
    try {
        boolean success = userService.updateProfileCompleted(username, completed) != null;
        if (!success) throw new Exception("No se pudo actualizar");
        return ResponseEntity.ok(Map.of("message", "Estado actualizado"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}


    /**
     * Endpoint para registrar nuevos usuarios
     * @param user datos del nuevo usuario
     * @return ResponseEntity con resultado del registro
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Verificar si el usuario ya existe
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "El nombre de usuario ya está en uso"));
            }      
            // Por defecto, los nuevos usuarios tienen el perfil incompleto
            user.setProfileCompleted(false);
            
            // Guardar el nuevo usuario
            User savedUser = userService.saveUser(user);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                        "message", "Usuario registrado correctamente",
                        "userId", savedUser.getId(),
                        "role", savedUser.getRole()));
                        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error durante el registro: " + e.getMessage()));
        }
    }
    
    /**
     * Endpoint para verificar si un usuario existe y obtener su información
     * @param username el nombre de usuario a verificar
     * @return ResponseEntity con información del usuario o error
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        try {
            Optional<User> optionalUser = userService.findByUsername(username);
            
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
            
            User user = optionalUser.get();
            
            return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Endpoint para actualizar el estado de completado del perfil
     * @param userId ID del usuario
     * @param completed estado de completado
     * @return ResponseEntity con resultado de la actualización
     */
    
    // Endpoint opcional para consultar todos los usuarios (si se requiere)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudieron obtener los usuarios: " + e.getMessage()));
        }
    }
    
}
