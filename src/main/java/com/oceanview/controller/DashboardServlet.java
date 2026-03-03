package com.oceanview.controller;

import com.oceanview.model.User;
import com.oceanview.service.ReservationService;
import com.oceanview.service.UserService;
import com.oceanview.dao.RoomDAO;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Dashboard Controller - serves admin dashboard with statistics.
 */
@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private UserService userService = new UserService();
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (!currentUser.hasAdminAccess()) {
            response.sendError(403);
            return;
        }

        // Gather stats
        int totalReservations = reservationService.getTotalReservations();
        double totalRevenue = reservationService.getTotalRevenue();
        int activeUsers = userService.getActiveUserCount();
        double occupancyRate = reservationService.getOccupancyRate();
        String topRoomType = reservationService.getTopRoomType();
        Map<String, Double> monthlyRevenue = reservationService.getMonthlyRevenue();
        Map<String, Integer> statusCounts = reservationService.getCountByStatus();

        request.setAttribute("totalReservations", totalReservations);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("occupancyRate", occupancyRate);
        request.setAttribute("topRoomType", topRoomType);
        request.setAttribute("monthlyRevenueJson", new Gson().toJson(monthlyRevenue));
        request.setAttribute("statusCountsJson", new Gson().toJson(statusCounts));

        request.getRequestDispatcher("/WEB-INF/views/admin/admin_dashboard.jsp").forward(request, response);
    }
}
