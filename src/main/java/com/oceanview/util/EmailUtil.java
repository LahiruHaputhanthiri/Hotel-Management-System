package com.oceanview.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Email utility using JavaMail API with Gmail SMTP.
 * Requires: Gmail account with 2-Step Verification + App Password.
 */
public class EmailUtil {

    // ============ Gmail SMTP Configuration ============
    // UPDATE THESE with your Gmail credentials
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_FROM = "your.email@gmail.com"; // Your Gmail
    private static final String EMAIL_PASSWORD = "your-app-password"; // Gmail App Password
    private static final String APP_NAME = "Ocean View Resort";
    private static final String BASE_URL = "http://localhost:8080/OceanViewResort";

    /**
     * Get a configured mail session.
     */
    private static Session getMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });
    }

    /**
     * Send a generic HTML email.
     */
    private static void sendEmail(String to, String subject, String htmlBody) {
        try {
            Session session = getMailSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM, APP_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Build the common email template wrapper.
     */
    private static String wrapInTemplate(String title, String content) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='utf-8'>" +
                "<style>" +
                "body { font-family: 'Poppins', Arial, sans-serif; background: #f5f5f5; margin: 0; padding: 20px; }" +
                ".email-container { max-width: 600px; margin: 0 auto; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }"
                +
                ".email-header { background: linear-gradient(135deg, #0D1B2A, #1B2838); padding: 30px; text-align: center; }"
                +
                ".email-header h1 { color: #C6A75E; margin: 0; font-size: 24px; letter-spacing: 2px; }" +
                ".email-header p { color: #fff; margin: 5px 0 0; font-size: 12px; letter-spacing: 3px; }" +
                ".email-body { padding: 40px 30px; color: #333; line-height: 1.6; }" +
                ".email-body h2 { color: #0D1B2A; margin-top: 0; }" +
                ".btn { display: inline-block; padding: 14px 32px; background: #C6A75E; color: #fff; text-decoration: none; border-radius: 6px; font-weight: 600; margin: 20px 0; }"
                +
                ".btn:hover { background: #b8963f; }" +
                ".email-footer { background: #0D1B2A; padding: 20px; text-align: center; color: #999; font-size: 12px; }"
                +
                ".detail-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #eee; }"
                +
                ".detail-label { font-weight: 600; color: #555; }" +
                ".detail-value { color: #0D1B2A; }" +
                "</style></head><body>" +
                "<div class='email-container'>" +
                "<div class='email-header'><h1>OCEAN VIEW RESORT</h1><p>GALLE &bull; SRI LANKA</p></div>" +
                "<div class='email-body'>" + content + "</div>" +
                "<div class='email-footer'>&copy; 2026 Ocean View Resort. All Rights Reserved.<br>Galle, Sri Lanka</div>"
                +
                "</div></body></html>";
    }

    // ============ Email Types ============

    /**
     * Send email verification link after registration.
     */
    public static void sendVerificationEmail(String to, String username, String token) {
        String verifyUrl = BASE_URL + "/auth?action=verify&token=" + token;
        String content = "<h2>Welcome to Ocean View Resort!</h2>" +
                "<p>Hello <strong>" + username + "</strong>,</p>" +
                "<p>Thank you for creating an account with us. Please verify your email address to get started.</p>" +
                "<a href='" + verifyUrl + "' class='btn'>Verify My Email</a>" +
                "<p style='color: #999; font-size: 13px;'>If you did not create this account, please ignore this email.</p>";
        sendEmail(to, "Verify Your Email - Ocean View Resort", wrapInTemplate("Verify Email", content));
    }

    /**
     * Send booking confirmation email.
     */
    public static void sendBookingConfirmation(String to, String guestName, String reservationNumber,
            String roomType, String checkIn, String checkOut, double totalAmount) {
        String content = "<h2>Booking Confirmed!</h2>" +
                "<p>Dear <strong>" + guestName + "</strong>,</p>" +
                "<p>Your reservation has been confirmed. Here are your booking details:</p>" +
                "<div style='background: #f8f8f8; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                "<div class='detail-row'><span class='detail-label'>Reservation #:</span><span class='detail-value'>"
                + reservationNumber + "</span></div>" +
                "<div class='detail-row'><span class='detail-label'>Room Type:</span><span class='detail-value'>"
                + roomType + "</span></div>" +
                "<div class='detail-row'><span class='detail-label'>Check-in:</span><span class='detail-value'>"
                + checkIn + "</span></div>" +
                "<div class='detail-row'><span class='detail-label'>Check-out:</span><span class='detail-value'>"
                + checkOut + "</span></div>" +
                "<div class='detail-row'><span class='detail-label'>Total Amount:</span><span class='detail-value'>$"
                + String.format("%.2f", totalAmount) + "</span></div>" +
                "</div>" +
                "<p>We look forward to welcoming you!</p>";
        sendEmail(to, "Booking Confirmed - " + reservationNumber, wrapInTemplate("Booking Confirmed", content));
    }

    /**
     * Send booking cancellation notice.
     */
    public static void sendCancellationNotice(String to, String guestName, String reservationNumber) {
        String content = "<h2>Booking Cancelled</h2>" +
                "<p>Dear <strong>" + guestName + "</strong>,</p>" +
                "<p>Your reservation <strong>" + reservationNumber + "</strong> has been cancelled.</p>" +
                "<p>If you have any questions or would like to rebook, please don't hesitate to contact us.</p>" +
                "<a href='" + BASE_URL + "' class='btn'>Visit Our Website</a>";
        sendEmail(to, "Booking Cancelled - " + reservationNumber, wrapInTemplate("Cancelled", content));
    }

    /**
     * Send password reset email.
     */
    public static void sendPasswordReset(String to, String username, String token) {
        String resetUrl = BASE_URL + "/auth?action=resetPasswordForm&token=" + token;
        String content = "<h2>Password Reset Request</h2>" +
                "<p>Hello <strong>" + username + "</strong>,</p>" +
                "<p>We received a request to reset your password. Click the button below to set a new password:</p>" +
                "<a href='" + resetUrl + "' class='btn'>Reset Password</a>" +
                "<p style='color: #999; font-size: 13px;'>This link will expire in 1 hour. If you didn't request this, please ignore this email.</p>";
        sendEmail(to, "Password Reset - Ocean View Resort", wrapInTemplate("Reset Password", content));
    }
}
