package com.oceanview.controller;

import com.oceanview.model.Payment;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.User;
import com.oceanview.service.ReservationService;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.util.CSRFTokenUtil;
import com.oceanview.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Reservation Controller - handles booking CRUD, bill generation.
 */
@WebServlet("/reservations/*")
public class ReservationServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private RoomDAO roomDAO = new RoomDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null)
            pathInfo = "/";

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        switch (pathInfo) {
            case "/new":
                showBookingForm(request, response);
                break;
            case "/my":
                showMyReservations(request, response, currentUser);
                break;
            case "/bill":
                showBill(request, response);
                break;
            case "/view":
                showReservationDetails(request, response, currentUser);
                break;
            default:
                // List all reservations (for admin)
                if (currentUser.hasAdminAccess()) {
                    listAllReservations(request, response);
                } else {
                    showMyReservations(request, response, currentUser);
                }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null)
            pathInfo = "/";

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        switch (pathInfo) {
            case "/create":
                createReservation(request, response, currentUser);
                break;
            case "/updateStatus":
                updateStatus(request, response, currentUser);
                break;
            case "/cancel":
                cancelReservation(request, response, currentUser);
                break;
            case "/pay":
                processPayment(request, response, currentUser);
                break;
            case "/delete":
                deleteReservation(request, response, currentUser);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/reservations/my");
        }
    }

    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Room> rooms = roomDAO.findAll();
        request.setAttribute("rooms", rooms);
        request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
        request.getRequestDispatcher("/WEB-INF/views/user/booking.jsp").forward(request, response);
    }

    private void showMyReservations(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Reservation> reservations = reservationService.getByUser(user.getId());
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher("/WEB-INF/views/user/my_reservations.jsp").forward(request, response);
    }

    private void showReservationDetails(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/reservations/my");
            return;
        }

        int id = Integer.parseInt(idStr);
        Reservation res = reservationService.getById(id);

        if (res == null || (!user.hasAdminAccess() && res.getUserId() != user.getId())) {
            request.setAttribute("error", "Reservation not found.");
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(request, response);
            return;
        }

        Payment payment = paymentDAO.findByReservation(id);
        request.setAttribute("reservation", res);
        request.setAttribute("payment", payment);
        request.getRequestDispatcher("/WEB-INF/views/user/reservation_details.jsp").forward(request, response);
    }

    private void showBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/reservations/my");
            return;
        }

        int id = Integer.parseInt(idStr);
        Reservation res = reservationService.getById(id);
        Payment payment = paymentDAO.findByReservation(id);

        if (payment == null) {
            payment = reservationService.calculateBill(id);
        }

        request.setAttribute("reservation", res);
        request.setAttribute("payment", payment);
        request.getRequestDispatcher("/WEB-INF/views/user/bill.jsp").forward(request, response);
    }

    private void createReservation(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        System.out.println("DEBUG: Starting createReservation for user: "
                + (currentUser != null ? currentUser.getUsername() : "NULL"));

        try {
            String guestName = request.getParameter("guestName");
            String guestEmail = request.getParameter("guestEmail");
            String address = request.getParameter("address");
            String contactNumber = request.getParameter("contactNumber");
            String roomType = request.getParameter("roomType");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            String numGuestsStr = request.getParameter("numGuests");
            String specialRequests = request.getParameter("specialRequests");

            // Validate
            if (ValidationUtil.isEmpty(guestName) || ValidationUtil.isEmpty(contactNumber) ||
                    ValidationUtil.isEmpty(roomType) || ValidationUtil.isEmpty(checkInStr)
                    || ValidationUtil.isEmpty(checkOutStr)) {
                request.setAttribute("error", "Please fill in all required fields.");
                showBookingForm(request, response);
                return;
            }

            Date checkIn = Date.valueOf(checkInStr);
            Date checkOut = Date.valueOf(checkOutStr);
            int numGuests = numGuestsStr != null ? Integer.parseInt(numGuestsStr) : 1;

            if (!ValidationUtil.isValidDateRange(checkIn, checkOut)) {
                request.setAttribute("error", "Check-out must be after check-in date.");
                showBookingForm(request, response);
                return;
            }

            String result = reservationService.createReservation(
                    currentUser.getId(), guestName, address, contactNumber, guestEmail,
                    roomType, checkIn, checkOut, numGuests, specialRequests);

            if (result.startsWith("SUCCESS:")) {
                String resNumber = result.split(":")[1];
                request.setAttribute("success", "Reservation " + resNumber + " created successfully!");
                showMyReservations(request, response, currentUser);
            } else {
                request.setAttribute("error", result);
                showBookingForm(request, response);
            }

        } catch (Throwable t) {
            System.err.println("CRITICAL ERROR in createReservation: " + t.getMessage());
            t.printStackTrace();
            request.setAttribute("error", "An internal server error occurred: " + t.getMessage());
            showBookingForm(request, response);
        }
    }

    private void listAllReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        List<Reservation> reservations;

        if (search != null && !search.trim().isEmpty()) {
            reservations = reservationService.search(search);
            request.setAttribute("searchQuery", search);
        } else {
            reservations = reservationService.getAll();
        }

        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher("/WEB-INF/views/admin/reservation_list.jsp").forward(request, response);
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        if (!currentUser.hasAdminAccess()) {
            response.sendError(403);
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");
        Reservation res = reservationService.getById(id);
        
        String targetEmail = null;
        if (res != null) {
            targetEmail = res.getGuestEmail();
            if (targetEmail == null || targetEmail.isEmpty()) {
                User resUser = new com.oceanview.service.UserService().getUserById(res.getUserId());
                if (resUser != null) targetEmail = resUser.getEmail();
            }
        }

        reservationService.updateStatus(id, status, targetEmail);
        response.sendRedirect(request.getContextPath() + "/reservations/?success=Status updated");
    }

    private void cancelReservation(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Reservation res = reservationService.getById(id);

        if (res == null || (!currentUser.hasAdminAccess() && res.getUserId() != currentUser.getId())) {
            response.sendError(403);
            return;
        }

        reservationService.updateStatus(id, "CANCELLED", currentUser.getEmail());

        if (currentUser.hasAdminAccess()) {
            response.sendRedirect(request.getContextPath() + "/reservations/?success=Reservation cancelled");
        } else {
            response.sendRedirect(request.getContextPath() + "/reservations/my?success=Reservation cancelled");
        }
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        if (!currentUser.hasAdminAccess()) {
            response.sendError(403);
            return;
        }

        int reservationId = Integer.parseInt(request.getParameter("id"));
        boolean success = reservationService.completePayment(reservationId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/reservations/bill?id=" + reservationId
                    + "&success=Payment marked as completed and thank you email sent");
        } else {
            response.sendRedirect(request.getContextPath() + "/reservations/bill?id=" + reservationId
                    + "&error=Failed to process payment");
        }
    }

    private void deleteReservation(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        if (!currentUser.hasAdminAccess()) {
            response.sendError(403);
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        reservationService.delete(id);
        response.sendRedirect(request.getContextPath() + "/reservations/?success=Reservation deleted");
    }
}
