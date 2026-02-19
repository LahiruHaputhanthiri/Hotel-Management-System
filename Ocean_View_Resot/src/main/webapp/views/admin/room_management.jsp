<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List, com.oceanview.model.Room" %>
        <% String ctx=request.getContextPath(); List<Room> rooms = (List<Room>) request.getAttribute("rooms");
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                String csrfToken = (String) request.getAttribute("csrfToken");
                %>
                <jsp:include page="/views/includes/header.jsp">
                    <jsp:param name="title" value="Room Management - Ocean View Resort" />
                </jsp:include>

                <div class="page-header">
                    <h1><i class="fas fa-bed" style="color: var(--accent);"></i> Room Management</h1>
                    <p class="breadcrumb-text">Add, edit, and manage hotel rooms</p>
                </div>

                <div class="content-wrapper">

                    <% if (error !=null) { %>
                        <div class="alert-custom alert-danger"><i class="fas fa-exclamation-circle"></i>
                            <%= error %>
                        </div>
                        <% } %>
                            <% if (success !=null) { %>
                                <div class="alert-custom alert-success" data-dismiss><i class="fas fa-check-circle"></i>
                                    <%= success %>
                                </div>
                                <% } %>

                                    <!-- Add Room Card -->
                                    <div class="content-card" style="margin-bottom: 30px;">
                                        <h4 style="margin-bottom: 20px;"><i class="fas fa-plus-circle"
                                                style="color: var(--accent);"></i> Add New Room</h4>
                                        <form action="<%= ctx %>/admin/rooms/" method="post">
                                            <input type="hidden" name="action" value="add">
                                            <div
                                                style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px;">
                                                <div class="form-group">
                                                    <label>Room Number</label>
                                                    <input type="text" name="roomNumber" class="form-control-custom"
                                                        placeholder="e.g. 101" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Room Type</label>
                                                    <select name="roomType" class="form-control-custom" required>
                                                        <option value="STANDARD">Standard</option>
                                                        <option value="DELUXE">Deluxe</option>
                                                        <option value="SUITE">Suite</option>
                                                        <option value="PENTHOUSE">Penthouse</option>
                                                        <option value="VILLA">Villa</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>Price Per Night ($)</label>
                                                    <input type="number" name="pricePerNight"
                                                        class="form-control-custom" step="0.01" placeholder="150.00"
                                                        required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Capacity</label>
                                                    <input type="number" name="capacity" class="form-control-custom"
                                                        min="1" max="10" value="2" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Floor</label>
                                                    <input type="number" name="floor" class="form-control-custom"
                                                        min="1" max="50" value="1" required>
                                                </div>
                                                <div class="form-group" style="display: flex; align-items: flex-end;">
                                                    <label
                                                        style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
                                                        <input type="checkbox" name="hasOceanView"
                                                            style="accent-color: var(--accent);"> Ocean View
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label>Description</label>
                                                <input type="text" name="description" class="form-control-custom"
                                                    placeholder="Room description...">
                                            </div>
                                            <button type="submit" class="btn-primary-custom"
                                                style="padding: 12px 28px;">
                                                <i class="fas fa-plus"></i> Add Room
                                            </button>
                                        </form>
                                    </div>

                                    <!-- Rooms Table -->
                                    <div style="overflow-x: auto;">
                                        <table class="data-table">
                                            <thead>
                                                <tr>
                                                    <th>Thumbnail</th>
                                                    <th>Room #</th>
                                                    <th>Type</th>
                                                    <th>Price/Night</th>
                                                    <th>Capacity</th>
                                                    <th>Floor</th>
                                                    <th>Ocean View</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% if (rooms !=null) { for (Room room : rooms) { %>
                                                    <tr>
                                                        <td>
                                                            <img src="<%= request.getContextPath() %>/images/room_<%= room.getRoomType().name().toLowerCase() %>.jpg"
                                                                style="width: 50px; height: 35px; object-fit: cover; border-radius: 4px; border: 1px solid var(--border);"
                                                                onerror="this.src='https://placehold.co/50x35?text=No+Img'">
                                                        </td>
                                                        <td><strong>
                                                                <%= room.getRoomNumber() %>
                                                            </strong></td>
                                                        <td>
                                                            <%= room.getTypeDisplay() %>
                                                        </td>
                                                        <td>$<%= String.format("%.2f", room.getPricePerNight()) %>
                                                        </td>
                                                        <td>
                                                            <%= room.getCapacity() %>
                                                        </td>
                                                        <td>
                                                            <%= room.getFloor() %>
                                                        </td>
                                                        <td>
                                                            <%= room.isHasOceanView()
                                                                ? "<i class='fas fa-check' style='color: var(--success);'></i>"
                                                                : "<i class='fas fa-times' style='color: var(--text-muted);'></i>"
                                                                %>
                                                        </td>
                                                        <td>
                                                            <span class="badge-status" style="<%= "
                                                                AVAILABLE".equals(room.getStatus().name())
                                                                ? "background:#D4EDDA;color:#155724;"
                                                                : "background:#F8D7DA;color:#721C24;" %>">
                                                                <%= room.getStatusDisplay() %>
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <div style="display: flex; gap: 6px;">
                                                                <form action="<%= ctx %>/admin/rooms/" method="post"
                                                                    style="display: inline;">
                                                                    <input type="hidden" name="action"
                                                                        value="updateStatus">
                                                                    <input type="hidden" name="roomId"
                                                                        value="<%= room.getId() %>">
                                                                    <select name="status" onchange="this.form.submit()"
                                                                        style="padding: 4px 8px; border-radius: 4px; border: 1px solid var(--border); font-size: 0.75rem;">
                                                                        <option value="">Status...</option>
                                                                        <option value="AVAILABLE">Available</option>
                                                                        <option value="OCCUPIED">Occupied</option>
                                                                        <option value="MAINTENANCE">Maintenance</option>
                                                                    </select>
                                                                </form>
                                                                <form action="<%= ctx %>/admin/rooms/" method="post"
                                                                    style="display: inline;"
                                                                    onsubmit="return confirmAction('Delete this room?');">
                                                                    <input type="hidden" name="action" value="delete">
                                                                    <input type="hidden" name="roomId"
                                                                        value="<%= room.getId() %>">
                                                                    <button type="submit" class="btn-dark-custom"
                                                                        style="padding: 6px 12px; font-size: 0.7rem; background: var(--danger);">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </form>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <% } } %>
                                            </tbody>
                                        </table>
                                    </div>
                </div>

                <jsp:include page="/views/includes/footer.jsp" />