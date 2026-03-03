package com.oceanview.model;

import java.sql.Timestamp;

/**
 * Payment model - represents a bill/payment for a reservation.
 * Bill = (nights × room_price) + 10% tax + 5% service charge
 */
public class Payment {

    public enum PaymentStatus {
        PENDING, COMPLETED, REFUNDED
    }

    public enum PaymentMethod {
        CASH, CARD, ONLINE
    }

    private int id;
    private int reservationId;
    private double amount; // Base amount (nights × price)
    private double tax; // 10% of amount
    private double serviceCharge; // 5% of amount
    private double total; // amount + tax + serviceCharge
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Timestamp createdAt;

    // Transient fields for display
    private String reservationNumber;
    private String guestName;

    // Default constructor
    public Payment() {
    }

    // Constructor with calculation
    public Payment(int reservationId, double baseAmount) {
        this.reservationId = reservationId;
        this.amount = baseAmount;
        this.tax = Math.round(baseAmount * 0.10 * 100.0) / 100.0;
        this.serviceCharge = Math.round(baseAmount * 0.05 * 100.0) / 100.0;
        this.total = Math.round((baseAmount + this.tax + this.serviceCharge) * 100.0) / 100.0;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentMethod = PaymentMethod.CASH;
    }

    // ============ Getters & Setters ============

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    // ============ Utility ============

    /**
     * Calculate bill breakdown from base amount.
     */
    public static Payment calculateBill(int reservationId, double nights, double pricePerNight) {
        double baseAmount = nights * pricePerNight;
        return new Payment(reservationId, baseAmount);
    }

    @Override
    public String toString() {
        return "Payment{id=" + id + ", reservationId=" + reservationId +
                ", total=" + total + ", status=" + paymentStatus + "}";
    }
}
