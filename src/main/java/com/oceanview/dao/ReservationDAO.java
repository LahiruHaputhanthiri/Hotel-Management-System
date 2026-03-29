package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Reservation operations.
 */
public class ReservationDAO {

    public boolean insert(Reservation res) {
        String sql = "INSERT INTO reservations (reservation_number, user_id, guest_name, address, contact_number, guest_email, room_id, room_type, check_in, check_out, num_guests, special_requests, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, res.getReservationNumber());
            ps.setInt(2, res.getUserId());
            ps.setString(3, res.getGuestName());
            ps.setString(4, res.getAddress());
            ps.setString(5, res.getContactNumber());
            ps.setString(6, res.getGuestEmail());
            if (res.getRoomId() > 0)
                ps.setInt(7, res.getRoomId());
            else
                ps.setNull(7, Types.INTEGER);
            ps.setString(8, res.getRoomType());
            ps.setDate(9, res.getCheckIn());
            ps.setDate(10, res.getCheckOut());
            ps.setInt(11, res.getNumGuests());
            ps.setString(12, res.getSpecialRequests());
            ps.setDouble(13, res.getTotalAmount());
            ps.setString(14, res.getStatus() != null ? res.getStatus().name() : "PENDING");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    res.setId(rs.getInt(1));
                    // Retrieve the auto-generated reservation number
                    Reservation created = findById(res.getId());
                    if (created != null) {
                        res.setReservationNumber(created.getReservationNumber());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting reservation: " + e.getMessage());
        }
        return false;
    }

    public Reservation findById(int id) {
        String sql = "SELECT r.*, u.username as user_name, rm.room_number, rm.price_per_night " +
                "FROM reservations r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapReservation(rs);
        } catch (SQLException e) {
            System.err.println("Error finding reservation: " + e.getMessage());
        }
        return null;
    }

    public Reservation findByReservationNumber(String number) {
        String sql = "SELECT r.*, u.username as user_name, rm.room_number, rm.price_per_night " +
                "FROM reservations r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.reservation_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapReservation(rs);
        } catch (SQLException e) {
            System.err.println("Error finding by reservation number: " + e.getMessage());
        }
        return null;
    }

    public List<Reservation> findByUser(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username as user_name, rm.room_number, rm.price_per_night " +
                "FROM reservations r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.user_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapReservation(rs));
        } catch (SQLException e) {
            System.err.println("Error finding user reservations: " + e.getMessage());
        }
        return list;
    }

    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username as user_name, rm.room_number, rm.price_per_night " +
                "FROM reservations r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN rooms rm ON r.room_id = rm.id " +
                "ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapReservation(rs));
        } catch (SQLException e) {
            System.err.println("Error finding all reservations: " + e.getMessage());
        }
        return list;
    }

    /**
     * Search reservations by keyword (guest name, reservation number, or contact).
     */
    public List<Reservation> search(String keyword) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username as user_name, rm.room_number, rm.price_per_night " +
                "FROM reservations r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.guest_name LIKE ? OR r.reservation_number LIKE ? OR r.contact_number LIKE ? " +
                "ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapReservation(rs));
        } catch (SQLException e) {
            System.err.println("Error searching reservations: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating reservation status: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Reservation res) {
        String sql = "UPDATE reservations SET guest_name=?, address=?, contact_number=?, guest_email=?, room_id=?, room_type=?, check_in=?, check_out=?, num_guests=?, special_requests=?, total_amount=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, res.getGuestName());
            ps.setString(2, res.getAddress());
            ps.setString(3, res.getContactNumber());
            ps.setString(4, res.getGuestEmail());
            if (res.getRoomId() > 0)
                ps.setInt(5, res.getRoomId());
            else
                ps.setNull(5, Types.INTEGER);
            ps.setString(6, res.getRoomType());
            ps.setDate(7, res.getCheckIn());
            ps.setDate(8, res.getCheckOut());
            ps.setInt(9, res.getNumGuests());
            ps.setString(10, res.getSpecialRequests());
            ps.setDouble(11, res.getTotalAmount());
            ps.setString(12, res.getStatus().name());
            ps.setInt(13, res.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
        return false;
    }

    // ============ Reports Queries ============

    public int getTotalReservations() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting reservations: " + e.getMessage());
        }
        return 0;
    }

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM reservations WHERE status IN ('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT')";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get monthly revenue for the current year (for Chart.js).
     * Returns a map of month name -> revenue.
     */
    public Map<String, Double> getMonthlyRevenue() {
        Map<String, Double> data = new LinkedHashMap<>();
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        for (String m : months)
            data.put(m, 0.0);

        String sql = "SELECT MONTH(created_at) as m, COALESCE(SUM(total_amount), 0) as revenue " +
                "FROM reservations WHERE YEAR(created_at) = YEAR(NOW()) " +
                "AND status IN ('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT') " +
                "GROUP BY MONTH(created_at) ORDER BY m";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int monthIdx = rs.getInt("m") - 1;
                if (monthIdx >= 0 && monthIdx < 12) {
                    data.put(months[monthIdx], rs.getDouble("revenue"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly revenue: " + e.getMessage());
        }
        return data;
    }

    /**
     * Get the most popular room type.
     */
    public String getTopRoomType() {
        String sql = "SELECT room_type, COUNT(*) as cnt FROM reservations GROUP BY room_type ORDER BY cnt DESC LIMIT 1";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getString("room_type");
        } catch (SQLException e) {
            System.err.println("Error getting top room type: " + e.getMessage());
        }
        return "N/A";
    }

    /**
     * Get reservations count by status.
     */
    public Map<String, Integer> getCountByStatus() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT status, COUNT(*) as cnt FROM reservations GROUP BY status";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("status"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting count by status: " + e.getMessage());
        }
        return data;
    }

    // ============ Helper ============

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation res = new Reservation();
        res.setId(rs.getInt("id"));
        res.setReservationNumber(rs.getString("reservation_number"));
        res.setUserId(rs.getInt("user_id"));
        res.setGuestName(rs.getString("guest_name"));
        res.setAddress(rs.getString("address"));
        res.setContactNumber(rs.getString("contact_number"));
        res.setGuestEmail(rs.getString("guest_email"));
        res.setRoomId(rs.getInt("room_id"));
        res.setRoomType(rs.getString("room_type"));
        res.setCheckIn(rs.getDate("check_in"));
        res.setCheckOut(rs.getDate("check_out"));
        res.setNumGuests(rs.getInt("num_guests"));
        res.setSpecialRequests(rs.getString("special_requests"));
        res.setTotalAmount(rs.getDouble("total_amount"));

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                res.setStatus(Reservation.Status.valueOf(statusStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                res.setStatus(Reservation.Status.PENDING);
            }
        } else {
            res.setStatus(Reservation.Status.PENDING);
        }

        res.setCreatedAt(rs.getTimestamp("created_at"));
        res.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Transient join fields
        try {
            res.setUserName(rs.getString("user_name"));
        } catch (SQLException ignored) {
        }
        try {
            res.setRoomNumber(rs.getString("room_number"));
        } catch (SQLException ignored) {
        }
        try {
            res.setPricePerNight(rs.getDouble("price_per_night"));
        } catch (SQLException ignored) {
        }

        return res;
    }
}
