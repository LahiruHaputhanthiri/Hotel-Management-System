package com.oceanview.dao;

import com.oceanview.model.Payment;
import com.oceanview.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Payment operations.
 */
public class PaymentDAO {

    public boolean insert(Payment payment) {
        String sql = "INSERT INTO payments (reservation_id, amount, tax, service_charge, total, payment_method, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getReservationId());
            ps.setDouble(2, payment.getAmount());
            ps.setDouble(3, payment.getTax());
            ps.setDouble(4, payment.getServiceCharge());
            ps.setDouble(5, payment.getTotal());
            ps.setString(6, payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : "CASH");
            ps.setString(7, payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : "PENDING");
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    payment.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting payment: " + e.getMessage());
        }
        return false;
    }

    public Payment findById(int id) {
        String sql = "SELECT p.*, r.reservation_number, r.guest_name FROM payments p " +
                "LEFT JOIN reservations r ON p.reservation_id = r.id WHERE p.id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapPayment(rs);
        } catch (SQLException e) {
            System.err.println("Error finding payment: " + e.getMessage());
        }
        return null;
    }

    public Payment findByReservation(int reservationId) {
        String sql = "SELECT p.*, r.reservation_number, r.guest_name FROM payments p " +
                "LEFT JOIN reservations r ON p.reservation_id = r.id WHERE p.reservation_id = ? ORDER BY p.created_at DESC LIMIT 1";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapPayment(rs);
        } catch (SQLException e) {
            System.err.println("Error finding payment for reservation: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, r.reservation_number, r.guest_name FROM payments p " +
                "LEFT JOIN reservations r ON p.reservation_id = r.id ORDER BY p.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapPayment(rs));
        } catch (SQLException e) {
            System.err.println("Error finding all payments: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE payments SET payment_status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
        }
        return false;
    }

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM payments WHERE payment_status = 'COMPLETED'";
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
     * Monthly revenue breakdown for charts.
     */
    public Map<String, Double> getMonthlyRevenue() {
        Map<String, Double> data = new LinkedHashMap<>();
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        for (String m : months)
            data.put(m, 0.0);

        String sql = "SELECT MONTH(created_at) as m, COALESCE(SUM(total), 0) as revenue " +
                "FROM payments WHERE YEAR(created_at) = YEAR(NOW()) AND payment_status = 'COMPLETED' " +
                "GROUP BY MONTH(created_at)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idx = rs.getInt("m") - 1;
                if (idx >= 0 && idx < 12)
                    data.put(months[idx], rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly revenue: " + e.getMessage());
        }
        return data;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setReservationId(rs.getInt("reservation_id"));
        p.setAmount(rs.getDouble("amount"));
        p.setTax(rs.getDouble("tax"));
        p.setServiceCharge(rs.getDouble("service_charge"));
        p.setTotal(rs.getDouble("total"));

        String methodStr = rs.getString("payment_method");
        if (methodStr != null) {
            try {
                p.setPaymentMethod(Payment.PaymentMethod.valueOf(methodStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                p.setPaymentMethod(Payment.PaymentMethod.CASH);
            }
        } else {
            p.setPaymentMethod(Payment.PaymentMethod.CASH);
        }

        String statusStr = rs.getString("payment_status");
        if (statusStr != null) {
            try {
                p.setPaymentStatus(Payment.PaymentStatus.valueOf(statusStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                p.setPaymentStatus(Payment.PaymentStatus.PENDING);
            }
        } else {
            p.setPaymentStatus(Payment.PaymentStatus.PENDING);
        }

        p.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            p.setReservationNumber(rs.getString("reservation_number"));
        } catch (SQLException ignored) {
        }
        try {
            p.setGuestName(rs.getString("guest_name"));
        } catch (SQLException ignored) {
        }
        return p;
    }
}
