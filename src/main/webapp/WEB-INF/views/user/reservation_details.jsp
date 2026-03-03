<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.oceanview.model.Reservation, com.oceanview.model.Payment" %>
        <% String ctx=request.getContextPath(); Reservation res=(Reservation) request.getAttribute("reservation");
            Payment payment=(Payment) request.getAttribute("payment"); %>
            <jsp:include page="/WEB-INF/views/includes/header.jsp">
                <jsp:param name="title" value="Reservation Details - Ocean View Resort" />
            </jsp:include>

            <div class="page-header">
                <h1>
                    <%= res !=null ? res.getReservationNumber() : "Reservation" %>
                </h1>
                <p class="breadcrumb-text">Reservation Details</p>
            </div>

            <div class="content-wrapper">
                <% if (res !=null) { %>
                    <div style="max-width: 800px; margin: 0 auto;">
                        <div class="content-card">
                            <div
                                style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
                                <h4><i class="fas fa-info-circle" style="color: var(--accent);"></i> Booking Information
                                </h4>
                                <span class="badge-status badge-<%= res.getStatus().name().toLowerCase().replace("
                                    _", "-" ) %>" style="font-size: 0.85rem; padding: 8px 20px;">
                                    <%= res.getStatusDisplay() %>
                                </span>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                                <div>
                                    <div class="invoice-row"><span class="label">Guest Name</span><span class="value">
                                            <%= res.getGuestName() %>
                                        </span></div>
                                    <div class="invoice-row"><span class="label">Contact</span><span class="value">
                                            <%= res.getContactNumber() %>
                                        </span></div>
                                    <div class="invoice-row"><span class="label">Address</span><span class="value">
                                            <%= res.getAddress() !=null ? res.getAddress() : "N/A" %>
                                        </span></div>
                                </div>
                                <div>
                                    <div class="invoice-row"><span class="label">Room Type</span><span class="value">
                                            <%= res.getRoomType() %>
                                        </span></div>
                                    <div class="invoice-row"><span class="label">Check-In</span><span class="value">
                                            <%= res.getCheckIn() %>
                                        </span></div>
                                    <div class="invoice-row"><span class="label">Check-Out</span><span class="value">
                                            <%= res.getCheckOut() %>
                                        </span></div>
                                    <div class="invoice-row"><span class="label">Nights</span><span class="value">
                                            <%= res.getNumberOfNights() %>
                                        </span></div>
                                </div>
                            </div>

                            <% if (res.getSpecialRequests() !=null && !res.getSpecialRequests().isEmpty()) { %>
                                <div
                                    style="margin-top: 20px; padding: 16px; background: var(--bg-cream); border-radius: var(--radius-sm);">
                                    <strong style="color: var(--accent);"><i class="fas fa-comment-dots"></i> Special
                                        Requests:</strong>
                                    <p style="margin-top: 4px;">
                                        <%= res.getSpecialRequests() %>
                                    </p>
                                </div>
                                <% } %>

                                    <div style="margin-top: 24px; display: flex; gap: 12px;">
                                        <a href="<%= ctx %>/reservations/bill?id=<%= res.getId() %>"
                                            class="btn-primary-custom" style="padding: 12px 24px;">
                                            <i class="fas fa-file-invoice-dollar"></i> View Bill
                                        </a>
                                        <a href="<%= ctx %>/reservations/my" class="btn-dark-custom"
                                            style="padding: 12px 24px;">
                                            <i class="fas fa-arrow-left"></i> Back
                                        </a>
                                    </div>
                        </div>
                    </div>
                    <% } %>
            </div>

            <jsp:include page="/WEB-INF/views/includes/footer.jsp" />