package com.oceanview.api;

import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.ReservationService;
import com.oceanview.dao.RoomDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API for Reservations and Room Availability.
 * Endpoints:
 * GET /api/reservations - list all reservations
 * GET /api/reservations?id=X - get single reservation
 * GET /api/rooms/available - check room availability (AJAX)
 * GET /api/rooms/price - get room price (AJAX)
 */
@WebServlet("/api/*")
public class ReservationAPI extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private RoomDAO roomDAO = new RoomDAO();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null)
            pathInfo = "/";

        try {
            if (pathInfo.startsWith("/reservations")) {
                handleGetReservations(request, out);
            } else if (pathInfo.startsWith("/rooms/available")) {
                handleRoomAvailability(request, out);
            } else if (pathInfo.startsWith("/rooms/price")) {
                handleRoomPrice(request, out);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Unknown endpoint");
                out.print(gson.toJson(error));
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            response.setStatus(500);
            out.print(gson.toJson(error));
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // POST /api/reservations - create
        Map<String, Object> result = new HashMap<>();
        result.put("message", "POST endpoint - use web form for creating reservations");
        result.put("status", "info");
        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        String status = request.getParameter("status");

        if (idStr != null && status != null) {
            int id = Integer.parseInt(idStr);
            boolean updated = reservationService.updateStatus(id, status, null);
            Map<String, Object> result = new HashMap<>();
            result.put("success", updated);
            result.put("message", updated ? "Status updated" : "Update failed");
            out.print(gson.toJson(result));
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            boolean deleted = reservationService.delete(id);
            Map<String, Object> result = new HashMap<>();
            result.put("success", deleted);
            out.print(gson.toJson(result));
        }
        out.flush();
    }

    private void handleGetReservations(HttpServletRequest request, PrintWriter out) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            Reservation res = reservationService.getById(Integer.parseInt(idStr));
            out.print(gson.toJson(res));
        } else {
            List<Reservation> list = reservationService.getAll();
            out.print(gson.toJson(list));
        }
    }

    private void handleRoomAvailability(HttpServletRequest request, PrintWriter out) {
        String roomType = request.getParameter("type");
        String checkInStr = request.getParameter("checkIn");
        String checkOutStr = request.getParameter("checkOut");

        Map<String, Object> result = new HashMap<>();

        if (roomType == null || checkInStr == null || checkOutStr == null) {
            result.put("error", "Missing parameters: type, checkIn, checkOut required");
            out.print(gson.toJson(result));
            return;
        }

        Date checkIn = Date.valueOf(checkInStr);
        Date checkOut = Date.valueOf(checkOutStr);

        List<Room> available = reservationService.getAvailableRooms(roomType, checkIn, checkOut);
        result.put("available", available.size() > 0);
        result.put("count", available.size());
        result.put("rooms", available);
        result.put("pricePerNight", reservationService.getRoomPrice(roomType));

        // Calculate total
        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        double price = reservationService.getRoomPrice(roomType);
        double base = nights * price;
        double total = base + (base * 0.10) + (base * 0.05);
        result.put("nights", nights);
        result.put("baseAmount", base);
        result.put("tax", base * 0.10);
        result.put("serviceCharge", base * 0.05);
        result.put("totalAmount", total);

        out.print(gson.toJson(result));
    }

    private void handleRoomPrice(HttpServletRequest request, PrintWriter out) {
        String roomType = request.getParameter("type");
        Map<String, Object> result = new HashMap<>();

        if (roomType != null) {
            double price = reservationService.getRoomPrice(roomType);
            result.put("type", roomType);
            result.put("pricePerNight", price);
        } else {
            result.put("error", "Room type required");
        }
        out.print(gson.toJson(result));
    }
}
