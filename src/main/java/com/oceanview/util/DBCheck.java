
package com.oceanview.util;

import java.sql.*;

public class DBCheck {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            System.out.println("DB_CONNECTED");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, email, role, is_verified FROM users");

            System.out.println("USERS_START");
            while (rs.next()) {
                System.out.println("USER: " + rs.getInt("id") + " | " + rs.getString("username") + " | "
                        + rs.getString("email") + " | " + rs.getString("role") + " | " + rs.getBoolean("is_verified"));
            }
            System.out.println("USERS_END");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
