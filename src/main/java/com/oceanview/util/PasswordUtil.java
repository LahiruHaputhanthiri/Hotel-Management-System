package com.oceanview.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utility using BCrypt for secure hashing and verification.
 */
public class PasswordUtil {

    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Hash a plain-text password using BCrypt.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify a plain-text password against a BCrypt hash.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error checking password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate password strength.
     * Requirements: 8+ chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char.
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8)
            return false;

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))
                hasUpper = true;
            else if (Character.isLowerCase(c))
                hasLower = true;
            else if (Character.isDigit(c))
                hasDigit = true;
            else
                hasSpecial = true;
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
