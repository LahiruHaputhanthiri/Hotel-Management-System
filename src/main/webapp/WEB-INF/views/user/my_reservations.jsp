<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List, com.oceanview.model.Reservation" %>
        <% String ctx=request.getContextPath(); List<Reservation> reservations = (List<Reservation>)
                request.getAttribute("reservations");
                String success = request.getParameter("success");
                %>
                <jsp:include page="/WEB-INF/views/includes/header.jsp">
                    <jsp:param name="title" value="My Reservations - Ocean View Resort" />
                </jsp:include>

                <div class="page-header">
                    <h1><i class="fas fa-suitcase" style="color: var(--accent);"></i> My Reservations</h1>
                    <p class="breadcrumb-text">View and manage your bookings</p>
                </div>

                <div class="content-wrapper">

                    <% if (success !=null) { %>
                        <div class="alert-custom alert-success" data-dismiss><i class="fas fa-check-circle"></i>
                            <%= success %>
                        </div>
                        <% } %>

                            <div style="text-align: right; margin-bottom: 24px;">
                                <a href="<%= ctx %>/reservations/new" class="btn-primary-custom"
                                    style="padding: 12px 28px;">
                                    <i class="fas fa-plus"></i> New Booking
                                </a>
                            </div>

                            <% if (reservations !=null && !reservations.isEmpty()) { %>
                                <div
                                    style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 20px;">
                                    <% for (Reservation res : reservations) { %>
                                        <div class="content-card" style="position: relative; overflow: hidden;">
                                            <div
                                                style="position: absolute; top: 0; right: 0; padding: 8px 18px; border-radius: 0 0 0 var(--radius-md);">
                                                <span
                                                    class="badge-status badge-<%= res.getStatus().name().toLowerCase().replace("
                                                    _", "-" ) %>">
                                                    <%= res.getStatusDisplay() %>
                                                </span>
                                            </div>

                                            <h4 style="color: var(--primary); margin-bottom: 4px;">
                                                <%= res.getReservationNumber() %>
                                            </h4>
                                            <p style="color: var(--accent); font-weight: 600; margin-bottom: 16px;">
                                                <%= res.getRoomType() %>
                                            </p>

                                            <div
                                                style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 16px;">
                                                <div>
                                                    <small style="color: var(--text-muted);">Check-In</small>
                                                    <p style="font-weight: 600; color: var(--text-dark);">
                                                        <%= res.getCheckIn() %>
                                                    </p>
                                                </div>
                                                <div>
                                                    <small style="color: var(--text-muted);">Check-Out</small>
                                                    <p style="font-weight: 600; color: var(--text-dark);">
                                                        <%= res.getCheckOut() %>
                                                    </p>
                                                </div>
                                            </div>

                                            <div
                                                style="display: flex; justify-content: space-between; align-items: center; padding-top: 16px; border-top: 1px solid var(--border);">
                                                <span
                                                    style="font-family: var(--font-heading); font-size: 1.3rem; font-weight: 700; color: var(--accent);">
                                                    $<%= String.format("%.2f", res.getTotalAmount()) %>
                                                </span>
                                                <div style="display: flex; gap: 8px;">
                                                    <a href="<%= ctx %>/reservations/view?id=<%= res.getId() %>"
                                                        class="btn-dark-custom"
                                                        style="padding: 8px 16px; font-size: 0.75rem;">
                                                        <i class="fas fa-eye"></i> View
                                                    </a>
                                                    <a href="<%= ctx %>/reservations/bill?id=<%= res.getId() %>"
                                                        class="btn-dark-custom"
                                                        style="padding: 8px 16px; font-size: 0.75rem;">
                                                        <i class="fas fa-receipt"></i> Bill
                                                    </a>
                                                    <% if ("PENDING".equals(res.getStatus().name()) || "CONFIRMED"
                                                        .equals(res.getStatus().name())) { %>
                                                        <form action="<%= ctx %>/reservations/cancel" method="post"
                                                            style="display:inline;"
                                                            onsubmit="return confirmAction('Cancel this reservation?');">
                                                            <input type="hidden" name="id" value="<%= res.getId() %>">
                                                            <button type="submit" class="btn-dark-custom"
                                                                style="padding: 8px 16px; font-size: 0.75rem; background: var(--danger);">
                                                                <i class="fas fa-times"></i>
                                                            </button>
                                                        </form>
                                                        <% } %>
                                                </div>
                                            </div>
                                        </div>
                                        <% } %>
                                </div>
                                <% } else { %>
                                    <div class="content-card" style="text-align: center; padding: 60px;">
                                        <i class="fas fa-suitcase-rolling"
                                            style="font-size: 4rem; color: var(--accent); margin-bottom: 20px;"></i>
                                        <h3>No Reservations Yet</h3>
                                        <p style="color: var(--text-muted); margin-bottom: 24px;">Start planning your
                                            dream vacation today!</p>
                                        <a href="<%= ctx %>/reservations/new" class="btn-primary-custom"><i
                                                class="fas fa-plus"></i> Book Your Stay</a>
                                    </div>
                                    <% } %>
                </div>

                <jsp:include page="/WEB-INF/views/includes/footer.jsp" />