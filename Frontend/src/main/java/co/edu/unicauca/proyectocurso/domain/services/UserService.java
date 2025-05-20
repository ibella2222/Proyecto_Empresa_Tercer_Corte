package co.edu.unicauca.proyectocurso.domain.services;

import co.edu.unicauca.proyectocurso.access.UserRepositoryImpl;
import co.edu.unicauca.proyectocurso.domain.entities.User;
import java.util.List;

public class UserService {

    private User user;
    private final UserRepositoryImpl userRepository;

    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listUser() {
        return userRepository.findAll();
    }

    public boolean registerUser(String username, String password, String role, int id) {
        if (userRepository.userExists(username)) {
            System.out.println("❌ El usuario ya existe.");
            return false;
        }
        return userRepository.registerUser(username, password, role, id);
    }

    public String validarUsuario(String username, String password) {
        String miRol = userRepository.getUserRole(username, password); 
        if (miRol == null) {
            return null;
        }
        user = userRepository.getUser(username);  // ✅ Usamos la instancia
        return miRol;
    }

    public boolean updateUser(String oldUsername, String newUsername, String newPassword, String newRole) {
        return userRepository.updateUser(oldUsername, newUsername, newPassword, newRole);
    }

    public List<User> searchUsers(String query) {
        return userRepository.searchUsers(query);
    }

    public List<User> searchUserss(String name, String role) {
        return userRepository.searchUserss(name, role);
    }

    public boolean deleteUser(String username) {
        return userRepository.deleteUser(username);
    }

    public boolean isProfileCompleted(String username) {
        return userRepository.isProfileCompleted(username);
    }

    public boolean updateProfileCompleted(String username, boolean completed) {
        return userRepository.updateProfileCompleted(username, completed);
    }

    public User getUser(String username) {
        return userRepository.getUser(username);  // ✅ También corregido aquí
    }
}
 