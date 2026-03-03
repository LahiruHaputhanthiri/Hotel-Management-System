package com.oceanview.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Server-side validation utility for all input fields.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,15}$");

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,30}$");

    /**
     * Validate email format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format (10-15 digits, optional +).
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim().replaceAll("[\\s()-]", "")).matches();
    }

    /**
     * Validate username (3-30 chars, alphanumeric + underscore).
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Check if a string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validate that check-in date is not in the past.
     */
    public static boolean isValidCheckInDate(Date checkIn) {
        if (checkIn == null)
            return false;
        return !checkIn.toLocalDate().isBefore(LocalDate.now());
    }

    /**
     * Validate that check-out is after check-in.
     */
    public static boolean isValidDateRange(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null)
            return false;
        return checkOut.after(checkIn);
    }

    /**
     * Validate that a string doesn't exceed max length.
     */
    public static boolean isWithinLength(String str, int maxLength) {
        return str == null || str.length() <= maxLength;
    }

    /**
     * Sanitize input - basic XSS prevention.
     */
    public static String sanitize(String input) {
        if (input == null)
            return null;
        return input.trim()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /**
     * Validate number of guests (min 1).
     */
    public static boolean isValidGuestCount(int count) {
        return count >= 1 && count <= 20;
    }
}
