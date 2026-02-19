<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List, com.oceanview.model.Reservation, com.oceanview.model.User" %>
        <% String ctx=request.getContextPath(); User dashUser=(User) session.getAttribute("user"); List<Reservation>
            reservations = (List<Reservation>) request.getAttribute("reservations");
                int count = (Integer) request.getAttribute("reservationCount");
                %>
                <jsp:include page="/views/includes/header.jsp">
                    <jsp:param name="title" value="My Dashboard - Ocean View Resort" />
                </jsp:include>

                <div class="page-header">
                    <h1>Welcome, <%= dashUser.getFullName() %>
                    </h1>
                    <p class="breadcrumb-text">Your personal dashboard</p>
                </div>

                <div class="content-wrapper">

                    <!-- Quick Stats -->
                    <div
                        style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 24px; margin-bottom: 36px;">
                        <div class="stat-card animate-fadeInUp">
                            <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
                            <div class="stat-value">
                                <%= count %>
                            </div>
                            <div class="stat-label">My Reservations</div>
                        </div>
                        <div class="stat-card animate-fadeInUp delay-1">
                            <div class="stat-icon"><i class="fas fa-user"></i></div>
                            <div class="stat-value">
                                <%= dashUser.getUsername() %>
                            </div>
                            <div class="stat-label">Account</div>
                        </div>
                        <div class="stat-card animate-fadeInUp delay-2" style="cursor: pointer;"
                            onclick="location.href='<%= ctx %>/reservations/new'">
                            <div class="stat-icon"><i class="fas fa-plus-circle"></i></div>
                            <div class="stat-value" style="font-size: 1.3rem;">Book Now</div>
                            <div class="stat-label">New Reservation</div>
                        </div>
                    </div>

                    <!-- Recent Reservations -->
                    <div class="content-card">
                        <div
                            style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                            <h4><i class="fas fa-history" style="color: var(--accent);"></i> Recent Reservations</h4>
                            <a href="<%= ctx %>/reservations/my" class="btn-dark-custom"
                                style="padding: 8px 20px; font-size: 0.75rem;">View All</a>
                        </div>

                        <% if (reservations !=null && !reservations.isEmpty()) { %>
                            <div style="overflow-x: auto;">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Reservation #</th>
                                            <th>Room Type</th>
                                            <th>Check-In</th>
                                            <th>Check-Out</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% int shown=0; for (Reservation res : reservations) { if (shown>= 5) break;
                                            shown++; %>
                                            <tr>
                                                <td><strong>
                                                        <%= res.getReservationNumber() %>
                                                    </strong></td>
                                                <td>
                                                    <%= res.getRoomType() %>
                                                </td>
                                                <td>
                                                    <%= res.getCheckIn() %>
                                                </td>
                                                <td>
                                                    <%= res.getCheckOut() %>
                                                </td>
                                                <td><span
                                                        class="badge-status badge-<%= res.getStatus().name().toLowerCase().replace("
                                                        _", "-" ) %>"><%= res.getStatusDisplay() %></span></td>
                                                <td>
                                                    <a href="<%= ctx %>/reservations/view?id=<%= res.getId() %>"
                                                        class="btn-dark-custom"
                                                        style="padding: 6px 12px; font-size: 0.7rem;"><i
                                                            class="fas fa-eye"></i></a>
                                                    <a href="<%= ctx %>/reservations/bill?id=<%= res.getId() %>"
                                                        class="btn-dark-custom"
                                                        style="padding: 6px 12px; font-size: 0.7rem;"><i
                                                            class="fas fa-file-invoice-dollar"></i></a>
                                                </td>
                                            </tr>
                                            <% } %>
                                    </tbody>
                                </table>
                            </div>
                            <% } else { %>
                                <div style="text-align: center; padding: 40px; color: var(--text-muted);">
                                    <i class="fas fa-suitcase-rolling"
                                        style="font-size: 3rem; margin-bottom: 16px; display: block; color: var(--accent);"></i>
                                    <p>You don't have any reservations yet.</p>
                                    <a href="<%= ctx %>/reservations/new" class="btn-primary-custom"
                                        style="margin-top: 16px; padding: 12px 28px;">
                                        <i class="fas fa-plus"></i> Book Your First Stay
                                    </a>
                                </div>
                                <% } %>
                    </div>
                </div>

                <jsp:include page="/views/includes/footer.jsp" />