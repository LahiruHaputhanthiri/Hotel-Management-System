package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Room operations.
 */
public class RoomDAO {

    public boolean insert(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, capacity, status, description, image_url, floor, has_ocean_view) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType().name());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus().name());
            ps.setString(6, room.getDescription());
            ps.setString(7, room.getImageUrl());
            ps.setInt(8, room.getFloor());
            ps.setBoolean(9, room.isHasOceanView());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    room.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting room: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    public Room findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRoom(rs);
        } catch (SQLException e) {
            System.err.println("Error finding room: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                rooms.add(mapRoom(rs));
        } catch (SQLException e) {
            System.err.println("Error finding all rooms: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return rooms;
    }

    public List<Room> findByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomType);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                rooms.add(mapRoom(rs));
        } catch (SQLException e) {
            System.err.println("Error finding rooms by type: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return rooms;
    }

    /**
     * Find available rooms for a given date range and type.
     */
    public List<Room> findAvailable(String roomType, Date checkIn, Date checkOut) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.* FROM rooms r WHERE r.status = 'AVAILABLE' " +
                "AND r.room_type = ? " +
                "AND r.id NOT IN (" +
                "  SELECT res.room_id FROM reservations res " +
                "  WHERE res.room_id IS NOT NULL " +
                "  AND res.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
                "  AND res.check_in < ? AND res.check_out > ?" +
                ") ORDER BY r.room_number";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomType);
            ps.setDate(2, checkOut);
            ps.setDate(3, checkIn);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                rooms.add(mapRoom(rs));
        } catch (SQLException e) {
            System.err.println("Error finding available rooms: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return rooms;
    }

    /**
     * Get count of available rooms by type.
     */
    public int countAvailableByType(String roomType) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_type = ? AND status = 'AVAILABLE'";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomType);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting available rooms: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return 0;
    }

    /**
     * Get price per night for a room type.
     */
    public double getPriceByType(String roomType) {
        String sql = "SELECT price_per_night FROM rooms WHERE room_type = ? LIMIT 1";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomType);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble("price_per_night");
        } catch (SQLException e) {
            System.err.println("Error getting room price: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return 0;
    }

    public boolean updateStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room status: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    public boolean updatePrice(int roomId, double newPrice) {
        String sql = "UPDATE rooms SET price_per_night = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, newPrice);
            ps.setInt(2, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room price: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_number=?, room_type=?, price_per_night=?, capacity=?, status=?, description=?, image_url=?, floor=?, has_ocean_view=? WHERE id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType().name());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus().name());
            ps.setString(6, room.getDescription());
            ps.setString(7, room.getImageUrl());
            ps.setInt(8, room.getFloor());
            ps.setBoolean(9, room.isHasOceanView());
            ps.setInt(10, room.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    /**
     * Get occupancy rate (occupied / total rooms).
     */
    public double getOccupancyRate() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM rooms WHERE status = 'OCCUPIED') * 100.0 / " +
                "(SELECT COUNT(*) FROM rooms) AS rate";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble("rate");
        } catch (SQLException e) {
            System.err.println("Error getting occupancy: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return 0;
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setRoomNumber(rs.getString("room_number"));
        String roomTypeStr = rs.getString("room_type");
        if (roomTypeStr != null) {
            try {
                room.setRoomType(Room.RoomType.valueOf(roomTypeStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                room.setRoomType(Room.RoomType.STANDARD);
            }
        }

        room.setPricePerNight(rs.getDouble("price_per_night"));
        room.setCapacity(rs.getInt("capacity"));

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                room.setStatus(Room.RoomStatus.valueOf(statusStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                room.setStatus(Room.RoomStatus.AVAILABLE);
            }
        }
        room.setDescription(rs.getString("description"));
        room.setImageUrl(rs.getString("image_url"));
        room.setFloor(rs.getInt("floor"));
        room.setHasOceanView(rs.getBoolean("has_ocean_view"));
        room.setCreatedAt(rs.getTimestamp("created_at"));
        return room;
    }
}
