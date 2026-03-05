package com.oceanview.util;

import java.sql.*;

/**
 * Utility to reset admin passwords and ensure they are verified.
 * Run this to fix login issues if the default seeds fail.
 */
public class ResetAdmin {
    public static void main(String[] args) {
        String newPassword = "Admin@123";
        String passwordHash = PasswordUtil.hashPassword(newPassword);

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            System.out.println("Connected to database.");

            String sql = "UPDATE users SET password_hash = ?, is_verified = TRUE WHERE username IN ('admin', 'superadmin')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, passwordHash);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("SUCCESS: Reset " + rows + " admin accounts.");
                System.out.println("You can now login with username 'admin' or 'superadmin' and password 'Admin@123'");
            } else {
                System.out.println(
                        "WARNING: No admin accounts found to reset. Please ensure you have run database_schema.sql first.");
            }

        } catch (SQLException e) {
            System.err.println("Error resetting admin passwords: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
