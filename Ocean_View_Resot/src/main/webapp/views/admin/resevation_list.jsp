<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List, com.oceanview.model.Reservation, com.oceanview.model.User" %>
        <% String ctx=request.getContextPath(); List<Reservation> reservations = (List<Reservation>)
                request.getAttribute("reservations");
                String searchQuery = (String) request.getAttribute("searchQuery");
                String success = request.getParameter("success");
                %>
                <jsp:include page="/views/includes/header.jsp">
                    <jsp:param name="title" value="All Reservations - Ocean View Resort" />
                </jsp:include>

                <div class="page-header">
                    <h1><i class="fas fa-calendar-alt" style="color: var(--accent);"></i> Reservations</h1>
                    <p class="breadcrumb-text">Manage all guest reservations</p>
                </div>

                <div class="content-wrapper">

                    <% if (success !=null) { %>
                        <div class="alert-custom alert-success" data-dismiss><i class="fas fa-check-circle"></i>
                            <%= success %>
                        </div>
                        <% } %>

                            <!-- Search & Actions -->
                            <div
                                style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
                                <form action="<%= ctx %>/reservations/" method="get" class="search-bar"
                                    style="flex: 1; max-width: 500px; margin-bottom: 0;">
                                    <input type="text" name="search"
                                        placeholder="Search by reservation #, guest name, phone..."
                                        value="<%= searchQuery != null ? searchQuery : "" %>">
                                    <button type="submit" class="btn-primary-custom" style="padding: 12px 24px;">
                                        <i class="fas fa-search"></i>
                                    </button>
                                </form>
                                <a href="<%= ctx %>/reservations/new" class="btn-primary-custom"
                                    style="padding: 12px 24px;">
                                    <i class="fas fa-plus"></i> New Booking
                                </a>
                            </div>

                            <!-- Reservations Table -->
                            <div style="overflow-x: auto;">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Reservation #</th>
                                            <th>Guest Name</th>
                                            <th>Room Type</th>
                                            <th>Check-In</th>
                                            <th>Check-Out</th>
                                            <th>Amount</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% if (reservations !=null && !reservations.isEmpty()) { for (Reservation res :
                                            reservations) { %>
                                            <tr>
                                                <td><strong>
                                                        <%= res.getReservationNumber() %>
                                                    </strong></td>
                                                <td>
                                                    <%= res.getGuestName() %>
                                                </td>
                                                <td>
                                                    <%= res.getRoomType() %>
                                                </td>
                                                <td>
                                                    <%= res.getCheckIn() %>
                                                </td>
                                                <td>
                                                    <%= res.getCheckOut() %>
                                                </td>
                                                <td><strong>$<%= String.format("%.2f", res.getTotalAmount()) %></strong>
                                                </td>
                                                <td>
                                                    <span
                                                        class="badge-status badge-<%= res.getStatus().name().toLowerCase().replace("
                                                        _", "-" ) %>">
                                                        <%= res.getStatusDisplay() %>
                                                    </span>
                                                </td>
                                                <td>
                                                    <div style="display: flex; gap: 6px;">
                                                        <a href="<%= ctx %>/reservations/view?id=<%= res.getId() %>"
                                                            class="btn-dark-custom"
                                                            style="padding: 6px 12px; font-size: 0.7rem;" title="View">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                        <a href="<%= ctx %>/reservations/bill?id=<%= res.getId() %>"
                                                            class="btn-dark-custom"
                                                            style="padding: 6px 12px; font-size: 0.7rem;" title="Bill">
                                                            <i class="fas fa-file-invoice-dollar"></i>
                                                        </a>
                                                        <% if (!"CANCELLED".equals(res.getStatus().name())) { %>
                                                            <form action="<%= ctx %>/reservations/updateStatus"
                                                                method="post" style="display:inline;">
                                                                <input type="hidden" name="id"
                                                                    value="<%= res.getId() %>">
                                                                <select name="status" onchange="this.form.submit()"
                                                                    style="padding: 4px 8px; border-radius: 4px; border: 1px solid var(--border); font-size: 0.75rem;">
                                                                    <option value="">Change...</option>
                                                                    <option value="CONFIRMED">Confirm</option>
                                                                    <option value="CHECKED_IN">Check In</option>
                                                                    <option value="CHECKED_OUT">Check Out</option>
                                                                    <option value="CANCELLED">Cancel</option>
                                                                </select>
                                                            </form>
                                                            <% } %>
                                                    </div>
                                                </td>
                                            </tr>
                                            <% } } else { %>
                                                <tr>
                                                    <td colspan="8"
                                                        style="text-align: center; padding: 40px; color: var(--text-muted);">
                                                        <i class="fas fa-inbox"
                                                            style="font-size: 2rem; margin-bottom: 12px; display: block;"></i>
                                                        No reservations found.
                                                    </td>
                                                </tr>
                                                <% } %>
                                    </tbody>
                                </table>
                            </div>
                </div>

                <jsp:include page="/views/includes/footer.jsp" />