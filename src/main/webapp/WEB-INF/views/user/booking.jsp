<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List" %>
        <%@ page import="com.oceanview.model.Room" %>
            <% String ctx=request.getContextPath(); String error=(String) request.getAttribute("error"); String
                csrfToken=(String) request.getAttribute("csrfToken"); List<Room> rooms = (List<Room>)
                    request.getAttribute("rooms");
                    %>
                    <jsp:include page="/WEB-INF/views/includes/header.jsp">
                        <jsp:param name="title" value="Book a Room - Ocean View Resort" />
                    </jsp:include>

                    <meta name="ctx" content="<%= ctx %>">

                    <div class="page-header">
                        <h1><i class="fas fa-concierge-bell" style="color: var(--accent);"></i> Book Your Stay</h1>
                        <p class="breadcrumb-text">Reserve your perfect ocean view room</p>
                    </div>

                    <div class="content-wrapper">

                        <% if (error !=null) { %>
                            <div class="alert-custom alert-danger"><i class="fas fa-exclamation-circle"></i>
                                <%= error %>
                            </div>
                            <% } %>

                                <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 30px;">

                                    <!-- Booking Form -->
                                    <div class="content-card">
                                        <h4 style="margin-bottom: 24px;"><i class="fas fa-pen"
                                                style="color: var(--accent);"></i> Reservation Details</h4>

                                        <form action="<%= ctx %>/reservations/create" method="post" id="bookingForm">
                                            <input type="hidden" name="csrfToken" value="<%= csrfToken %>">

                                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                                                <div class="form-group">
                                                    <label>Guest Name *</label>
                                                    <input type="text" name="guestName" class="form-control-custom"
                                                        placeholder="Full name" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Contact Number *</label>
                                                    <input type="tel" name="contactNumber" class="form-control-custom"
                                                        placeholder="+94 77 123 4567" required>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label>Address</label>
                                                <input type="text" name="address" class="form-control-custom"
                                                    placeholder="Street address, City">
                                            </div>

                                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                                                <div class="form-group">
                                                    <label>Room Type *</label>
                                                    <select name="roomType" id="roomType" class="form-control-custom"
                                                        required onchange="updateRoomPrice(); checkAvailability();">
                                                        <option value="">Select Room Type</option>
                                                        <option value="STANDARD">Standard - $150/night</option>
                                                        <option value="DELUXE">Deluxe - $250/night</option>
                                                        <option value="SUITE">Suite - $450/night</option>
                                                        <option value="PENTHOUSE">Penthouse - $800/night</option>
                                                        <option value="VILLA">Villa - $1200/night</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>Number of Guests</label>
                                                    <input type="number" name="numGuests" class="form-control-custom"
                                                        min="1" max="10" value="2">
                                                </div>
                                            </div>

                                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                                                <div class="form-group">
                                                    <label>Check-In Date *</label>
                                                    <input type="date" name="checkIn" id="checkIn"
                                                        class="form-control-custom" required
                                                        onchange="checkAvailability();">
                                                </div>
                                                <div class="form-group">
                                                    <label>Check-Out Date *</label>
                                                    <input type="date" name="checkOut" id="checkOut"
                                                        class="form-control-custom" required
                                                        onchange="checkAvailability();">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label>Special Requests</label>
                                                <textarea name="specialRequests" class="form-control-custom" rows="3"
                                                    placeholder="Any special requirements? (e.g., extra pillows, late check-in)"></textarea>
                                            </div>

                                            <!-- Availability Result -->
                                            <div id="availabilityResult" style="margin-bottom: 20px;"></div>

                                            <button type="submit" class="btn-primary-custom"
                                                style="width: 100%; text-align: center; padding: 16px;">
                                                <i class="fas fa-check"></i> Confirm Reservation
                                            </button>
                                        </form>
                                    </div>

                                    <!-- Sidebar -->
                                    <div>
                                        <div class="content-card" style="position: sticky; top: 100px;">
                                            <h4 style="margin-bottom: 16px;"><i class="fas fa-info-circle"
                                                    style="color: var(--accent);"></i> Booking Info</h4>

                                            <div id="roomPreview" class="room-card"
                                                style="margin-bottom: 20px; display: none;">
                                                <div class="card-image" style="height: 180px;">
                                                    <img src="" id="previewImg" alt="Room Preview">
                                                </div>
                                            </div>

                                            <div id="pricePerNight"
                                                style="font-size: 1.5rem; font-weight: 700; color: var(--primary); margin-bottom: 16px;">
                                                Select a room type
                                            </div>

                                            <div style="border-top: 1px solid var(--border); padding-top: 16px;">
                                                <p
                                                    style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 8px;">
                                                    <i class="fas fa-percentage"
                                                        style="color: var(--accent); width: 20px;"></i>
                                                    <strong>Tax:</strong> 10% of base
                                                </p>
                                                <p
                                                    style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 8px;">
                                                    <i class="fas fa-concierge-bell"
                                                        style="color: var(--accent); width: 20px;"></i>
                                                    <strong>Service:</strong> 5% of base
                                                </p>
                                                <p
                                                    style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 8px;">
                                                    <i class="fas fa-clock"
                                                        style="color: var(--accent); width: 20px;"></i>
                                                    Check-in: 2:00 PM
                                                </p>
                                                <p
                                                    style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 8px;">
                                                    <i class="fas fa-clock"
                                                        style="color: var(--accent); width: 20px;"></i>
                                                    Check-out: 12:00 PM
                                                </p>
                                            </div>

                                            <div
                                                style="border-top: 1px solid var(--border); padding-top: 16px; margin-top: 16px;">
                                                <h5 style="margin-bottom: 12px;">Cancellation Policy</h5>
                                                <p
                                                    style="font-size: 0.85rem; color: var(--text-muted); line-height: 1.6;">
                                                    Free cancellation up to 24 hours before check-in.
                                                    Late cancellations may incur a one-night charge.
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                    </div>

                    <script>
                        // Set minimum dates
                        var today = new Date().toISOString().split('T')[0];
                        document.getElementById('checkIn').setAttribute('min', today);
                        document.getElementById('checkOut').setAttribute('min', today);

                        document.getElementById('checkIn').addEventListener('change', function () {
                            document.getElementById('checkOut').setAttribute('min', this.value);
                        });
                    </script>

                    <jsp:include page="/WEB-INF/views/includes/footer.jsp" />