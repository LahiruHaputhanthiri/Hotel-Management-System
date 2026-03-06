package com.oceanview.controller;

import com.oceanview.model.User;
import com.oceanview.service.UserService;
import com.oceanview.util.CSRFTokenUtil;
import com.oceanview.util.PasswordUtil;
import com.oceanview.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * User Management Controller - SuperAdmin only.
 * Handles admin account creation and user listing.
 */
@WebServlet("/superadmin/users/*")
public class UserManagementServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (!currentUser.isSuperAdmin()) {
            response.sendError(403);
            return;
        }

        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
        request.getRequestDispatcher("/WEB-INF/views/admin/user_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("createAdmin".equals(action)) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");

            if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(email) ||
                    ValidationUtil.isEmpty(password) || ValidationUtil.isEmpty(fullName)) {
                request.setAttribute("error", "All fields are required.");
            } else if (!PasswordUtil.isStrongPassword(password)) {
                request.setAttribute("error",
                        "Password must be 8+ chars with uppercase, lowercase, number, and special character.");
            } else {
                String result = userService.createAdmin(username, email, password, fullName, phone);
                if ("SUCCESS".equals(result)) {
                    request.setAttribute("success", "Admin account created successfully!");
                } else {
                    request.setAttribute("error", result);
                }
            }
        } else if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            userService.deleteUser(userId);
            request.setAttribute("success", "User deleted successfully.");
        }

        doGet(request, response);
    }
}
