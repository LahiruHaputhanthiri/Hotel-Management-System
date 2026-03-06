package com.oceanview.filter;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication & Role-Based Access Control (RBAC) Filter.
 * Intercepts all requests and enforces authentication and role restrictions.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    // Pages accessible without login
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/", "/index.jsp", "/login.jsp", "/register.jsp",
            "/auth", "/css/", "/js/", "/images/", "/fonts/",
            "/verify.jsp", "/forgot_password.jsp", "/reset_password.jsp",
            "/api/", "/help", "/diagnostics");

    // Paths requiring ADMIN or SUPERADMIN
    private static final List<String> ADMIN_PATHS = Arrays.asList(
            "/admin/");

    // Paths requiring SUPERADMIN only
    private static final List<String> SUPERADMIN_PATHS = Arrays.asList(
            "/superadmin/");

    private static final int SESSION_TIMEOUT_MINUTES = 15;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get sanitized path
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        path = path.replaceAll("//+", "/").toLowerCase();
        if (!path.startsWith("/"))
            path = "/" + path;

        // Allow public paths (case-insensitive)
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check session
        HttpSession session = httpRequest.getSession(false);
        User currentUser = null;

        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }

        // Try remember-me cookie if no session
        if (currentUser == null) {
            currentUser = checkRememberMeCookie(httpRequest);
            if (currentUser != null) {
                session = httpRequest.getSession(true);
                session.setAttribute("user", currentUser);
            }
        }

        // 1. Force Authentication for all non-public pages
        if (currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=auth_required");
            return;
        }

        // 2. Strict Admin Authorization
        if (isAdminPath(path)) {
            if (!currentUser.hasAdminAccess()) {
                System.err.println("SECURITY ALERT: Unauthorized Admin Access Attempt by: " + currentUser.getUsername()
                        + " on " + path);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin privileges required.");
                return;
            }
        }

        // 3. Strict SuperAdmin Authorization
        if (isSuperAdminPath(path)) {
            if (!currentUser.isSuperAdmin()) {
                System.err.println("SECURITY ALERT: Unauthorized SuperAdmin Access Attempt by: "
                        + currentUser.getUsername() + " on " + path);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Access Denied: SuperAdmin privileges required.");
                return;
            }
        }

        // Set user attribute and security headers
        httpRequest.setAttribute("currentUser", currentUser);
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com; font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com;");

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        String p = path.toLowerCase();
        if (p.equals("/") || p.isEmpty())
            return true;

        return PUBLIC_PATHS.stream().anyMatch(pub -> {
            String pubLower = pub.toLowerCase();
            return pubLower.endsWith("/") ? p.startsWith(pubLower) : p.equals(pubLower);
        });
    }

    private boolean isAdminPath(String path) {
        String p = path.toLowerCase();
        return ADMIN_PATHS.stream().anyMatch(admin -> p.startsWith(admin.toLowerCase()));
    }

    private boolean isSuperAdminPath(String path) {
        String p = path.toLowerCase();
        return SUPERADMIN_PATHS.stream().anyMatch(sa -> p.startsWith(sa.toLowerCase()));
    }

    private User checkRememberMeCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMe".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && !token.isEmpty()) {
                        UserDAO userDAO = new UserDAO();
                        return userDAO.findByRememberToken(token);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
