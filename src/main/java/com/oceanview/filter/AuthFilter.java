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

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Normalize path: replace multiple slashes with one
        path = path.replaceAll("//+", "/");
        if (!path.startsWith("/"))
            path = "/" + path;

        // Allow public paths
        if (isPublicPath(path)) {
            // System.out.println("DEBUG AuthFilter: Allowed public path: " + path);
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
                session.setMaxInactiveInterval(SESSION_TIMEOUT_MINUTES * 60);
            }
        }

        // No authenticated user → redirect to login
        if (currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        // Check admin access
        if (isAdminPath(path) && !currentUser.hasAdminAccess()) {
            httpRequest.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(httpRequest, httpResponse);
            return;
        }

        // Check superadmin access
        if (isSuperAdminPath(path) && !currentUser.isSuperAdmin()) {
            httpRequest.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(httpRequest, httpResponse);
            return;
        }

        // Set user attribute for JSPs
        httpRequest.setAttribute("currentUser", currentUser);

        // Set security headers
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        if (path.equals("/") || path.isEmpty())
            return true;

        for (String pub : PUBLIC_PATHS) {
            if (pub.equals("/"))
                continue;
            if (pub.endsWith("/")) {
                if (path.startsWith(pub))
                    return true;
            } else {
                if (path.equals(pub) || path.equals(pub + "/"))
                    return true;
            }
        }
        return false;
    }

    private boolean isAdminPath(String path) {
        for (String admin : ADMIN_PATHS) {
            if (path.startsWith(admin))
                return true;
        }
        return false;
    }

    private boolean isSuperAdminPath(String path) {
        for (String sa : SUPERADMIN_PATHS) {
            if (path.startsWith(sa))
                return true;
        }
        return false;
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
