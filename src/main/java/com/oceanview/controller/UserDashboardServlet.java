package com.oceanview.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * User Dashboard Controller - serves user's personal dashboard.
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private com.oceanview.service.ReservationService reservationService = new com.oceanview.service.ReservationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        com.oceanview.model.User user = (com.oceanview.model.User) request.getSession().getAttribute("user");

        var reservations = reservationService.getByUser(user.getId());
        request.setAttribute("reservations", reservations);
        request.setAttribute("reservationCount", reservations.size());

        request.getRequestDispatcher("/WEB-INF/views/user/user_dashboard.jsp").forward(request, response);
    }
}
