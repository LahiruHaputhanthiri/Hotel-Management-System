package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.model.User;
import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * User Dashboard Controller - serves user's personal dashboard.
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Reservation> reservations = reservationService.getByUser(user.getId());
        request.setAttribute("reservations", reservations);
        request.setAttribute("reservationCount", reservations != null ? reservations.size() : 0);

        request.getRequestDispatcher("/WEB-INF/views/user/user_dashboard.jsp").forward(request, response);
    }
}
