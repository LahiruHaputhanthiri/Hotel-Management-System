package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.EmailUtil;
import com.oceanview.util.PasswordUtil;

import java.util.List;
import java.util.UUID;

/**
 * User service - handles registration, authentication, verification, and
 * password reset.
 * Implements Factory pattern for role-based user creation.
 */
public class UserService {

    private UserDAO userDAO = new UserDAO();

    /**
     * Register a new user with email verification.
     */
    public String register(String username, String email, String password, String fullName, String phone) {
        // Check if email already exists
        if (userDAO.findByEmail(email) != null) {
            return "Email is already registered.";
        }
        // Check if username already exists
        if (userDAO.findByUsername(username) != null) {
            return "Username is already taken.";
        }

        // Hash password
        String passwordHash = PasswordUtil.hashPassword(password);

        // Create user using Factory pattern
        User user = User.createUser(username, email, passwordHash, fullName, phone, "USER");

        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        // Save to database
        if (userDAO.insert(user)) {
            // Send verification email
            try {
                EmailUtil.sendVerificationEmail(email, username, token);
            } catch (Throwable t) {
                System.err.println("CRITICAL: Email sending failed: " + t.getMessage());
                t.printStackTrace();
            }
            return "SUCCESS";
        }
        return "Registration failed. Please try again.";
    }

    /**
     * Create admin account (SuperAdmin only).
     */
    public String createAdmin(String username, String email, String password, String fullName, String phone) {
        if (userDAO.findByEmail(email) != null) {
            return "Email is already registered.";
        }
        if (userDAO.findByUsername(username) != null) {
            return "Username is already taken.";
        }

        String passwordHash = PasswordUtil.hashPassword(password);
        User user = User.createUser(username, email, passwordHash, fullName, phone, "ADMIN");

        if (userDAO.insert(user)) {
            return "SUCCESS";
        }
        return "Failed to create admin account.";
    }

    /**
     * Authenticate user.
     */
    public User login(String usernameOrEmail, String password) {
        User user = userDAO.findByEmail(usernameOrEmail);
        if (user == null) {
            user = userDAO.findByUsername(usernameOrEmail);
        }
        if (user == null)
            return null;

        if (!user.isVerified())
            return null;

        if (PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Check if user exists but not verified (for showing appropriate error
     * message).
     */
    public boolean isUnverifiedUser(String usernameOrEmail) {
        User user = userDAO.findByEmail(usernameOrEmail);
        if (user == null) {
            user = userDAO.findByUsername(usernameOrEmail);
        }
        return user != null && !user.isVerified();
    }

    /**
     * Verify email via token.
     */
    public boolean verifyEmail(String token) {
        User user = userDAO.findByVerificationToken(token);
        if (user != null) {
            return userDAO.updateVerificationStatus(user.getId(), true);
        }
        return false;
    }

    /**
     * Initiate password reset - send email with token.
     */
    public String forgotPassword(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            return "No account found with that email.";
        }

        String token = UUID.randomUUID().toString();
        if (userDAO.setResetToken(user.getId(), token)) {
            try {
                EmailUtil.sendPasswordReset(email, user.getUsername(), token);
            } catch (Throwable t) {
                return "Failed to send reset email: " + t.getMessage();
            }
            return "SUCCESS";
        }
        return "Failed to process password reset.";
    }

    /**
     * Reset password using token.
     */
    public String resetPassword(String token, String newPassword) {
        User user = userDAO.findByResetToken(token);
        if (user == null) {
            return "Invalid or expired reset link.";
        }

        String hash = PasswordUtil.hashPassword(newPassword);
        if (userDAO.updatePassword(user.getId(), hash)) {
            return "SUCCESS";
        }
        return "Failed to reset password.";
    }

    /**
     * Set remember-me token.
     */
    public String setRememberMe(int userId) {
        String token = UUID.randomUUID().toString();
        userDAO.setRememberToken(userId, token);
        return token;
    }

    /**
     * Auto-login via remember-me cookie.
     */
    public User loginWithRememberToken(String token) {
        return userDAO.findByRememberToken(token);
    }

    /**
     * Clear remember-me token on logout.
     */
    public void clearRememberMe(int userId) {
        userDAO.setRememberToken(userId, null);
    }

    // ============ Admin Operations ============

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userDAO.findByRole(role);
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public int getActiveUserCount() {
        return userDAO.countActiveUsers();
    }

    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }
}
