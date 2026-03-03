package com.oceanview.model;

import java.sql.Timestamp;

/**
 * User model - represents a system user with role-based access.
 * Supports SUPERADMIN, ADMIN, and USER roles.
 */
public class User {
    
    public enum Role {
        SUPERADMIN, ADMIN, USER
    }
    
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private Role role;
    private boolean isVerified;
    private String verificationToken;
    private String resetToken;
    private Timestamp resetTokenExpiry;
    private String rememberToken;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public User() {}
    
    // Constructor for registration
    public User(String username, String email, String passwordHash, String fullName, String phone, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.isVerified = false;
    }
    
    // ============ Factory Pattern ============
    
    /**
     * Factory method - creates a User based on the specified role.
     */
    public static User createUser(String username, String email, String passwordHash, 
                                   String fullName, String phone, String roleStr) {
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            role = Role.USER;
        }
        
        User user = new User(username, email, passwordHash, fullName, phone, role);
        
        // SuperAdmin and Admin are auto-verified
        if (role == Role.SUPERADMIN || role == Role.ADMIN) {
            user.setVerified(true);
        }
        
        return user;
    }
    
    // ============ Getters & Setters ============
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
    
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    
    public Timestamp getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(Timestamp resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }
    
    public String getRememberToken() { return rememberToken; }
    public void setRememberToken(String rememberToken) { this.rememberToken = rememberToken; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    // ============ Utility Methods ============
    
    public boolean isSuperAdmin() { return role == Role.SUPERADMIN; }
    public boolean isAdmin() { return role == Role.ADMIN; }
    public boolean isUser() { return role == Role.USER; }
    public boolean hasAdminAccess() { return role == Role.SUPERADMIN || role == Role.ADMIN; }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + 
               "', role=" + role + ", verified=" + isVerified + "}";
    }
}
