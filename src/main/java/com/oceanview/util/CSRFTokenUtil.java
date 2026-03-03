package com.oceanview.util;

import java.security.SecureRandom;
import java.util.Base64;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * CSRF Token utility for protecting POST forms against CSRF attacks.
 */
public class CSRFTokenUtil {

    private static final String CSRF_TOKEN_ATTR = "csrfToken";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generate a new CSRF token and store it in the session.
     */
    public static String generateToken(HttpServletRequest request) {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        HttpSession session = request.getSession();
        session.setAttribute(CSRF_TOKEN_ATTR, token);
        return token;
    }

    /**
     * Validate CSRF token from form submission against session token.
     */
    public static boolean validateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return false;

        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        String formToken = request.getParameter("csrfToken");

        if (sessionToken == null || formToken == null)
            return false;

        return sessionToken.equals(formToken);
    }

    /**
     * Get the current CSRF token from the session.
     */
    public static String getToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        return (String) session.getAttribute(CSRF_TOKEN_ATTR);
    }
}
