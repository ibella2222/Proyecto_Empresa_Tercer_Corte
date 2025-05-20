package com.example.login.DTO;

public class LoginResponse {
    private String message;
    private String username;
    private int userId;
    private String role;
    private boolean profileCompleted;
    
    // Constructor vacío
    public LoginResponse() {
    }
    
    // Constructor con mensaje
    public LoginResponse(String message) {
        this.message = message;
    }
    
    // Constructor completo
    public LoginResponse(String message, String username, int userId, String role, boolean profileCompleted) {
        this.message = message;
        this.username = username;
        this.userId = userId;
        this.role = role;
        this.profileCompleted = profileCompleted;
    }
    
    // Getters & Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isProfileCompleted() {
        return profileCompleted;
    }
    
    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
}