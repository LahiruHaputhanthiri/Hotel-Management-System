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

/**
 * Authentication Controller - handles login, register, logout, email
 * verification, password reset.
 */
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private UserService userService = new UserService();
    private static final int SESSION_TIMEOUT_MINUTES = 15;
    private static final int REMEMBER_ME_MAX_AGE = 7 * 24 * 60 * 60; // 7 days

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null)
            action = "";

        switch (action) {
            case "logout":
                handleLogout(request, response);
                break;
            case "verify":
                handleEmailVerification(request, response);
                break;
            case "resetPasswordForm":
                String token = request.getParameter("token");
                request.setAttribute("resetToken", token);
                request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
                request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null)
            action = "";

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "forgotPassword":
                handleForgotPassword(request, response);
                break;
            case "resetPassword":
                handleResetPassword(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (ValidationUtil.isEmpty(usernameOrEmail) || ValidationUtil.isEmpty(password)) {
            request.setAttribute("error", "Please fill in all fields.");
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Check if unverified
        if (userService.isUnverifiedUser(usernameOrEmail)) {
            request.setAttribute("error", "Please verify your email before logging in. Check your inbox.");
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userService.login(usernameOrEmail, password);

        if (user != null) {
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole().name());
            session.setMaxInactiveInterval(SESSION_TIMEOUT_MINUTES * 60);

            // Handle remember-me
            if ("on".equals(rememberMe)) {
                String token = userService.setRememberMe(user.getId());
                Cookie cookie = new Cookie("rememberMe", token);
                cookie.setMaxAge(REMEMBER_ME_MAX_AGE);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            // Redirect based on role
            switch (user.getRole()) {
                case SUPERADMIN:
                case ADMIN:
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/user/dashboard");
                    break;
            }
        } else {
            request.setAttribute("error", "Invalid username/email or password.");
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");

            // Validate
            if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(email) ||
                    ValidationUtil.isEmpty(password) || ValidationUtil.isEmpty(fullName)) {
                setRegisterError(request, response, "Please fill in all required fields.");
                return;
            }
            if (!ValidationUtil.isValidUsername(username)) {
                setRegisterError(request, response,
                        "Username must be 3-30 characters, alphanumeric and underscores only.");
                return;
            }
            if (!ValidationUtil.isValidEmail(email)) {
                setRegisterError(request, response, "Please enter a valid email address.");
                return;
            }
            if (!PasswordUtil.isStrongPassword(password)) {
                setRegisterError(request, response,
                        "Password must be 8+ characters with uppercase, lowercase, number, and special character.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                setRegisterError(request, response, "Passwords do not match.");
                return;
            }
            if (phone != null && !phone.isEmpty() && !ValidationUtil.isValidPhone(phone)) {
                setRegisterError(request, response, "Please enter a valid phone number.");
                return;
            }

            String result = userService.register(username, email, password, fullName, phone);

            if ("SUCCESS".equals(result)) {
                request.setAttribute("success",
                        "Registration successful! Please check your email to verify your account.");
                request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                setRegisterError(request, response, result);
            }
        } catch (Throwable t) {
            System.err.println("CRITICAL ERROR in handleRegister: " + t.getMessage());
            t.printStackTrace();
            request.setAttribute("error", "An internal error occurred during registration: " + t.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userService.clearRememberMe(user.getId());
            }
            session.invalidate();
        }

        // Clear remember-me cookie
        Cookie cookie = new Cookie("rememberMe", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.sendRedirect(request.getContextPath() + "/login.jsp?msg=loggedOut");
    }

    private void handleEmailVerification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        boolean verified = userService.verifyEmail(token);

        if (verified) {
            request.setAttribute("success", "Email verified successfully! You can now log in.");
        } else {
            request.setAttribute("error", "Invalid or expired verification link.");
        }
        request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        if (ValidationUtil.isEmpty(email) || !ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.getRequestDispatcher("/forgot_password.jsp").forward(request, response);
            return;
        }

        String result = userService.forgotPassword(email);
        if ("SUCCESS".equals(result)) {
            request.setAttribute("success", "Password reset link has been sent to your email.");
        } else {
            request.setAttribute("error", result);
        }
        request.getRequestDispatcher("/forgot_password.jsp").forward(request, response);
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!PasswordUtil.isStrongPassword(newPassword)) {
            request.setAttribute("error",
                    "Password must be 8+ characters with uppercase, lowercase, number, and special character.");
            request.setAttribute("resetToken", token);
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.setAttribute("resetToken", token);
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
            return;
        }

        String result = userService.resetPassword(token, newPassword);
        if ("SUCCESS".equals(result)) {
            request.setAttribute("success", "Password reset successfully! You can now log in.");
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", result);
            request.setAttribute("resetToken", token);
            request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
            request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
        }
    }

    private void setRegisterError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
        // Preserve form data
        request.setAttribute("regUsername", request.getParameter("username"));
        request.setAttribute("regEmail", request.getParameter("email"));
        request.setAttribute("regFullName", request.getParameter("fullName"));
        request.setAttribute("regPhone", request.getParameter("phone"));
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
