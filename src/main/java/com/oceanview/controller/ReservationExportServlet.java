package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet to export all reservations as a CSV file (Excel compatible).
 */
@WebServlet("/admin/export/reservations")
public class ReservationExportServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Reservation> reservations = reservationDAO.findAll();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=Reservations_Report_" + System.currentTimeMillis() + ".csv");

        PrintWriter writer = response.getWriter();

        // CSV Header
        writer.println(
                "Reservation Number,Guest Name,Room Type,Check-in,Check-out,Num Guests,Total Amount,Status,Created At");

        // CSV Data
        for (Reservation res : reservations) {
            writer.print(escapeCSV(res.getReservationNumber()) + ",");
            writer.print(escapeCSV(res.getGuestName()) + ",");
            writer.print(escapeCSV(res.getRoomType()) + ",");
            writer.print(res.getCheckIn() + ",");
            writer.print(res.getCheckOut() + ",");
            writer.print(res.getNumGuests() + ",");
            writer.print(String.format("%.2f", res.getTotalAmount()) + ",");
            writer.print(res.getStatus().name() + ",");
            writer.println(res.getCreatedAt());
        }

        writer.flush();
        writer.close();
    }

    /**
     * Helper to escape CSV special characters.
     */
    private String escapeCSV(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
