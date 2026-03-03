package com.oceanview.controller;

import com.oceanview.model.Room;
import com.oceanview.model.User;
import com.oceanview.dao.RoomDAO;
import com.oceanview.util.CSRFTokenUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Room Management Controller - Admin/SuperAdmin only.
 */
@WebServlet("/admin/rooms/*")
public class RoomServlet extends HttpServlet {

    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Room> rooms = roomDAO.findAll();
        request.setAttribute("rooms", rooms);
        request.setAttribute("csrfToken", CSRFTokenUtil.generateToken(request));
        request.getRequestDispatcher("/WEB-INF/views/admin/room_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updatePrice".equals(action)) {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            double newPrice = Double.parseDouble(request.getParameter("newPrice"));
            roomDAO.updatePrice(roomId, newPrice);
            request.setAttribute("success", "Price updated successfully!");
        } else if ("updateStatus".equals(action)) {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String status = request.getParameter("status");
            roomDAO.updateStatus(roomId, status);
            request.setAttribute("success", "Room status updated!");
        } else if ("add".equals(action)) {
            Room room = new Room();
            room.setRoomNumber(request.getParameter("roomNumber"));
            room.setRoomType(Room.RoomType.valueOf(request.getParameter("roomType")));
            room.setPricePerNight(Double.parseDouble(request.getParameter("pricePerNight")));
            room.setCapacity(Integer.parseInt(request.getParameter("capacity")));
            room.setDescription(request.getParameter("description"));
            room.setFloor(Integer.parseInt(request.getParameter("floor")));
            room.setHasOceanView("on".equals(request.getParameter("hasOceanView")));
            room.setStatus(Room.RoomStatus.AVAILABLE);

            if (roomDAO.insert(room)) {
                request.setAttribute("success", "Room added successfully!");
            } else {
                request.setAttribute("error", "Failed to add room.");
            }
        } else if ("delete".equals(action)) {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            roomDAO.delete(roomId);
            request.setAttribute("success", "Room deleted.");
        }

        doGet(request, response);
    }
}
