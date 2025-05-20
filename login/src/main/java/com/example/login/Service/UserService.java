package com.example.login.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.login.DTO.LoginResponse;
import com.example.login.Entities.User;
import com.example.login.Messaging.UserRegistrationSender;
import com.example.login.Repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRegistrationSender registrationSender;




    /**
     * Busca un usuario por su nombre de usuario
     * @param username el nombre de usuario a buscar
     * @return Optional con el usuario si existe, vacío si no
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserService(UserRepository userRepository, UserRegistrationSender registrationSender) {
        this.userRepository = userRepository;
        this.registrationSender = registrationSender;
    }
    
    /**
     * Valida si la contraseña proporcionada coincide con la del usuario
     * @param user el usuario cuya contraseña se va a validar
     * @param rawPassword la contraseña sin encriptar a validar
     * @return true si la contraseña es correcta, false en caso contrario
     */
    public boolean validatePassword(User user, String rawPassword) {
        return rawPassword.equals(user.getPassword()); // Comparación simple
    }
    
    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     * @param username el nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Guarda un nuevo usuario en la base de datos, encriptando su contraseña
     * @param user el usuario a guardar
     * @return el usuario guardado con su ID generado
     */
    public User saveUser(User user) {

        User savedUser = userRepository.save(user);
        LoginResponse loginResponseDTO = new LoginResponse(
            "Registro exitoso",            // mensaje
            savedUser.getUsername(),       // nombre de usuario
            savedUser.getId(),             // id del usuario
            savedUser.getRole(),           // rol del usuario
            savedUser.isProfileCompleted() // estado de perfil completado
       );
       logger.info("Enviando notificación a la cola de mensajes");
       // Envia el mensaje a la cola utilizando el sender correspondiente.
       registrationSender.sendUserRegistration(loginResponseDTO);
       logger.info("Notificación enviada correctamente");
        return savedUser;
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
    /**
     * Valida las credenciales de un usuario
     * @param username nombre de usuario
     * @param password contraseña
     * @return User si las credenciales son válidas, null en caso contrario
     */
    public User validateUser(String username, String password) {
        Optional<User> optionalUser = findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (validatePassword(user, password)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario
     * @return Optional con el usuario si existe, vacío si no
     */
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
    
    /**
     * Actualiza el estado de completado del perfil de un usuario
     * @param userId ID del usuario
     * @param completed nuevo estado de completado
     * @return usuario actualizado
     */
    public User updateProfileCompleted(String username, boolean completed) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfileCompleted(completed);
            return userRepository.save(user);
        }
        throw new RuntimeException("Usuario no encontrado con username: " + username);
    }
    
    
    /**
     * Actualiza el rol de un usuario
     * @param userId ID del usuario
     * @param role nuevo rol
     * @return usuario actualizado
     */
    public User updateUserRole(int userId, String role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(role);
            return userRepository.save(user);
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + userId);
    }

}