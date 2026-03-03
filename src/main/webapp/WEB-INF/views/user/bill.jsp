<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.oceanview.model.Reservation, com.oceanview.model.Payment" %>
        <% String ctx=request.getContextPath(); Reservation res=(Reservation) request.getAttribute("reservation");
            Payment payment=(Payment) request.getAttribute("payment"); %>
            <jsp:include page="/WEB-INF/views/includes/header.jsp">
                <jsp:param name="title" value="Invoice - Ocean View Resort" />
            </jsp:include>

            <div class="page-header">
                <h1><i class="fas fa-file-invoice-dollar" style="color: var(--accent);"></i> Invoice</h1>
                <p class="breadcrumb-text">
                    <%= res !=null ? res.getReservationNumber() : "" %>
                </p>
            </div>

            <div class="content-wrapper">
                <% if (res !=null && payment !=null) { %>
                    <div class="invoice">
                        <div class="invoice-header">
                            <h2>Ocean View Resort & Spa</h2>
                            <p>Galle Road, Colombo 3, Sri Lanka | +94 11 234 5678</p>
                            <p style="margin-top: 12px; font-size: 0.85rem; opacity: 0.8;">
                                Invoice for Reservation: <strong>
                                    <%= res.getReservationNumber() %>
                                </strong>
                            </p>
                        </div>

                        <div class="invoice-body">
                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 30px;">
                                <div>
                                    <h5 style="color: var(--accent); margin-bottom: 8px;">Guest Details</h5>
                                    <p><strong>
                                            <%= res.getGuestName() %>
                                        </strong></p>
                                    <p>
                                        <%= res.getContactNumber() %>
                                    </p>
                                    <p>
                                        <%= res.getAddress() !=null ? res.getAddress() : "" %>
                                    </p>
                                </div>
                                <div style="text-align: right;">
                                    <h5 style="color: var(--accent); margin-bottom: 8px;">Booking Details</h5>
                                    <p>Room: <strong>
                                            <%= res.getRoomType() %>
                                        </strong></p>
                                    <p>
                                        <%= res.getCheckIn() %> → <%= res.getCheckOut() %>
                                    </p>
                                    <p>
                                        <%= res.getNumberOfNights() %> Night(s)
                                    </p>
                                </div>
                            </div>

                            <hr style="border-color: var(--border);">

                            <div class="invoice-row">
                                <span class="label">Base Amount (<%= res.getNumberOfNights() %> nights)</span>
                                <span class="value">$<%= String.format("%.2f", payment.getAmount()) %></span>
                            </div>
                            <div class="invoice-row">
                                <span class="label">Tax (10%)</span>
                                <span class="value">$<%= String.format("%.2f", payment.getTax()) %></span>
                            </div>
                            <div class="invoice-row">
                                <span class="label">Service Charge (5%)</span>
                                <span class="value">$<%= String.format("%.2f", payment.getServiceCharge()) %></span>
                            </div>

                            <div class="invoice-total">
                                <span class="label">TOTAL AMOUNT</span>
                                <span class="value">$<%= String.format("%.2f", payment.getTotal()) %></span>
                            </div>

                            <div style="margin-top: 30px; text-align: center;">
                                <span
                                    class="badge-status badge-<%= payment.getPaymentStatus() != null ? payment.getPaymentStatus().toLowerCase() : "pending" %>"
                                    style="font-size: 0.9rem; padding: 10px 24px;">
                                    Payment: <%= payment.getPaymentStatus() !=null ? payment.getPaymentStatus()
                                        : "PENDING" %>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="no-print"
                        style="text-align: center; margin-top: 24px; display: flex; gap: 12px; justify-content: center;">
                        <button onclick="printInvoice()" class="btn-dark-custom" style="padding: 12px 28px;">
                            <i class="fas fa-print"></i> Print
                        </button>
                        <a href="<%= ctx %>/reservations/bill/pdf?resNum=<%= res.getReservationNumber() %>"
                            class="btn-primary-custom" style="padding: 12px 28px;">
                            <i class="fas fa-file-pdf"></i> Download PDF
                        </a>
                        <a href="<%= ctx %>/reservations/my" class="btn-dark-custom" style="padding: 12px 28px;">
                            <i class="fas fa-arrow-left"></i> Back
                        </a>
                    </div>
                    <% } else { %>
                        <div class="content-card" style="text-align: center; padding: 40px;">
                            <p style="color: var(--text-muted);">Invoice not available.</p>
                        </div>
                        <% } %>
            </div>

            <jsp:include page="/WEB-INF/views/includes/footer.jsp" />