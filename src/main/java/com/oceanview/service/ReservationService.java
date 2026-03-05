package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.model.Payment;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.util.EmailUtil;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Reservation service - handles booking logic, bill calculation, and status
 * management.
 */
public class ReservationService {

    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    private static final double TAX_RATE = 0.10; // 10%
    private static final double SERVICE_RATE = 0.05; // 5%

    /**
     * Create a new reservation with automatic bill calculation.
     */
    public String createReservation(int userId, String guestName, String address, String contactNumber,
            String roomType, Date checkIn, Date checkOut, int numGuests,
            String specialRequests, String userEmail) {

        System.out.println("DEBUG: ReservationService.createReservation for " + guestName + " (" + roomType + ")");

        // Validate dates
        if (checkOut.before(checkIn) || checkOut.equals(checkIn)) {
            return "Check-out date must be after check-in date.";
        }

        // Find an available room
        List<Room> availableRooms = roomDAO.findAvailable(roomType, checkIn, checkOut);
        if (availableRooms.isEmpty()) {
            return "No rooms of type " + roomType + " available for the selected dates.";
        }

        Room selectedRoom = availableRooms.get(0);

        // Calculate bill
        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        double baseAmount = nights * selectedRoom.getPricePerNight();
        double tax = Math.round(baseAmount * TAX_RATE * 100.0) / 100.0;
        double serviceCharge = Math.round(baseAmount * SERVICE_RATE * 100.0) / 100.0;
        double totalAmount = baseAmount + tax + serviceCharge;

        // Build reservation
        Reservation reservation = new Reservation.Builder()
                .userId(userId)
                .guestName(guestName)
                .address(address)
                .contactNumber(contactNumber)
                .roomId(selectedRoom.getId())
                .roomType(roomType)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .numGuests(numGuests)
                .specialRequests(specialRequests)
                .totalAmount(totalAmount)
                .status(Reservation.Status.PENDING)
                .build();

        if (reservationDAO.insert(reservation)) {
            // Create payment record
            Payment payment = new Payment(reservation.getId(), baseAmount);
            paymentDAO.insert(payment);

            // Send confirmation email
            try {
                EmailUtil.sendBookingConfirmation(userEmail, guestName, reservation.getReservationNumber(),
                        roomType, checkIn.toString(), checkOut.toString(), totalAmount);
            } catch (Throwable t) {
                System.err.println("Booking email failed: " + t.getMessage());
            }

            return "SUCCESS:" + reservation.getReservationNumber();
        }
        return "Failed to create reservation.";
    }

    /**
     * Calculate bill for a reservation.
     */
    public Payment calculateBill(int reservationId) {
        Reservation res = reservationDAO.findById(reservationId);
        if (res == null)
            return null;

        double pricePerNight = res.getPricePerNight();
        if (pricePerNight <= 0) {
            pricePerNight = roomDAO.getPriceByType(res.getRoomType());
        }

        long nights = res.getNumberOfNights();
        return Payment.calculateBill(reservationId, nights, pricePerNight);
    }

    /**
     * Calculate price preview (for AJAX).
     */
    public double calculatePreview(String roomType, Date checkIn, Date checkOut) {
        double price = roomDAO.getPriceByType(roomType);
        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        double base = nights * price;
        return base + (base * TAX_RATE) + (base * SERVICE_RATE);
    }

    /**
     * Update reservation status with email notification.
     */
    public boolean updateStatus(int reservationId, String status, String userEmail) {
        boolean updated = reservationDAO.updateStatus(reservationId, status);

        if (updated && "CANCELLED".equals(status) && userEmail != null) {
            Reservation res = reservationDAO.findById(reservationId);
            if (res != null) {
                try {
                    EmailUtil.sendCancellationNotice(userEmail, res.getGuestName(), res.getReservationNumber());
                } catch (Exception e) {
                    System.err.println("Cancellation email failed: " + e.getMessage());
                }
            }
        }
        return updated;
    }

    // ============ Delegated Queries ============

    public Reservation getById(int id) {
        return reservationDAO.findById(id);
    }

    public Reservation getByNumber(String number) {
        return reservationDAO.findByReservationNumber(number);
    }

    public List<Reservation> getByUser(int userId) {
        return reservationDAO.findByUser(userId);
    }

    public List<Reservation> getAll() {
        return reservationDAO.findAll();
    }

    public List<Reservation> search(String keyword) {
        return reservationDAO.search(keyword);
    }

    public boolean delete(int id) {
        return reservationDAO.delete(id);
    }

    // ============ Dashboard Stats ============

    public int getTotalReservations() {
        return reservationDAO.getTotalReservations();
    }

    public double getTotalRevenue() {
        return reservationDAO.getTotalRevenue();
    }

    public Map<String, Double> getMonthlyRevenue() {
        return reservationDAO.getMonthlyRevenue();
    }

    public String getTopRoomType() {
        return reservationDAO.getTopRoomType();
    }

    public Map<String, Integer> getCountByStatus() {
        return reservationDAO.getCountByStatus();
    }

    // ============ Room Operations ============

    public List<Room> getAvailableRooms(String type, Date checkIn, Date checkOut) {
        return roomDAO.findAvailable(type, checkIn, checkOut);
    }

    public double getRoomPrice(String roomType) {
        return roomDAO.getPriceByType(roomType);
    }

    public double getOccupancyRate() {
        return roomDAO.getOccupancyRate();
    }
}
