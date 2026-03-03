package com.oceanview.model;

import java.sql.Timestamp;

/**
 * Room model - represents a hotel room with type, pricing, and status.
 */
public class Room {

    public enum RoomType {
        STANDARD, DELUXE, SUITE, PRESIDENTIAL, PENTHOUSE, VILLA
    }

    public enum RoomStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE
    }

    private int id;
    private String roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private int capacity;
    private RoomStatus status;
    private String description;
    private String imageUrl;
    private int floor;
    private boolean hasOceanView;
    private Timestamp createdAt;

    // Default constructor
    public Room() {
    }

    // Full constructor
    public Room(String roomNumber, RoomType roomType, double pricePerNight, int capacity,
            String description, int floor, boolean hasOceanView) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.description = description;
        this.floor = floor;
        this.hasOceanView = hasOceanView;
        this.status = RoomStatus.AVAILABLE;
    }

    // ============ Getters & Setters ============

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean isHasOceanView() {
        return hasOceanView;
    }

    public void setHasOceanView(boolean hasOceanView) {
        this.hasOceanView = hasOceanView;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ============ Utility Methods ============

    public String getRoomTypeDisplay() {
        switch (roomType) {
            case STANDARD:
                return "Standard Room";
            case DELUXE:
                return "Deluxe Room";
            case SUITE:
                return "Suite";
            case PRESIDENTIAL:
                return "Presidential Suite";
            case PENTHOUSE:
                return "Penthouse";
            case VILLA:
                return "Villa";
            default:
                return roomType.name();
        }
    }

    public String getTypeDisplay() {
        return getRoomTypeDisplay();
    }

    public String getStatusDisplay() {
        switch (status) {
            case AVAILABLE:
                return "Available";
            case OCCUPIED:
                return "Occupied";
            case MAINTENANCE:
                return "Under Maintenance";
            default:
                return status.name();
        }
    }

    @Override
    public String toString() {
        return "Room{id=" + id + ", number='" + roomNumber + "', type=" + roomType +
                ", price=" + pricePerNight + ", status=" + status + "}";
    }
}
