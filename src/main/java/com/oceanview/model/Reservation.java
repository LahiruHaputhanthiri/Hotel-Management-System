package com.oceanview.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Reservation model - uses Builder pattern for complex object creation.
 */
public class Reservation {

    public enum Status {
        PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    }

    private int id;
    private String reservationNumber;
    private int userId;
    private String guestName;
    private String address;
    private String contactNumber;
    private int roomId;
    private String roomType;
    private Date checkIn;
    private Date checkOut;
    private int numGuests;
    private String specialRequests;
    private double totalAmount;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Transient fields (not stored, used for display)
    private String userName;
    private String roomNumber;
    private double pricePerNight;

    // Default constructor
    public Reservation() {
    }

    // Private constructor for Builder
    private Reservation(Builder builder) {
        this.id = builder.id;
        this.reservationNumber = builder.reservationNumber;
        this.userId = builder.userId;
        this.guestName = builder.guestName;
        this.address = builder.address;
        this.contactNumber = builder.contactNumber;
        this.roomId = builder.roomId;
        this.roomType = builder.roomType;
        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
        this.numGuests = builder.numGuests;
        this.specialRequests = builder.specialRequests;
        this.totalAmount = builder.totalAmount;
        this.status = builder.status;
    }

    // ============ Builder Pattern ============

    public static class Builder {
        private int id;
        private String reservationNumber;
        private int userId;
        private String guestName;
        private String address;
        private String contactNumber;
        private int roomId;
        private String roomType;
        private Date checkIn;
        private Date checkOut;
        private int numGuests = 1;
        private String specialRequests;
        private double totalAmount;
        private Status status = Status.PENDING;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder reservationNumber(String reservationNumber) {
            this.reservationNumber = reservationNumber;
            return this;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder guestName(String guestName) {
            this.guestName = guestName;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public Builder roomId(int roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder roomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder checkIn(Date checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public Builder checkOut(Date checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public Builder numGuests(int numGuests) {
            this.numGuests = numGuests;
            return this;
        }

        public Builder specialRequests(String specialRequests) {
            this.specialRequests = specialRequests;
            return this;
        }

        public Builder totalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }

    // ============ Getters & Setters ============

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Transient getters/setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    // ============ Utility Methods ============

    public long getNumberOfNights() {
        if (checkIn != null && checkOut != null) {
            long diff = checkOut.getTime() - checkIn.getTime();
            return diff / (1000 * 60 * 60 * 24);
        }
        return 0;
    }

    public String getStatusDisplay() {
        if (status == null)
            return "Unknown";
        switch (status) {
            case PENDING:
                return "Pending";
            case CONFIRMED:
                return "Confirmed";
            case CHECKED_IN:
                return "Checked In";
            case CHECKED_OUT:
                return "Checked Out";
            case CANCELLED:
                return "Cancelled";
            default:
                return status.name();
        }
    }

    public String getStatusBadgeClass() {
        if (status == null)
            return "bg-secondary";
        switch (status) {
            case PENDING:
                return "bg-warning text-dark";
            case CONFIRMED:
                return "bg-success";
            case CHECKED_IN:
                return "bg-info";
            case CHECKED_OUT:
                return "bg-secondary";
            case CANCELLED:
                return "bg-danger";
            default:
                return "bg-secondary";
        }
    }

    @Override
    public String toString() {
        return "Reservation{id=" + id + ", number='" + reservationNumber +
                "', guest='" + guestName + "', status=" + status + "}";
    }
}
