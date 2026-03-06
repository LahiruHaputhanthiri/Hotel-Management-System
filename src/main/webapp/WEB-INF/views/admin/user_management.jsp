<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.List, com.oceanview.model.User" %>
        <% String ctx=request.getContextPath(); List<User> users = (List<User>) request.getAttribute("users");
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                String csrfToken = (String) request.getAttribute("csrfToken");
                %>
                <jsp:include page="/WEB-INF/views/includes/header.jsp">
                    <jsp:param name="title" value="User Management - Ocean View Resort" />
                </jsp:include>

                <div class="page-header">
                    <h1><i class="fas fa-users-cog" style="color: var(--accent);"></i> User Management</h1>
                    <p class="breadcrumb-text">SuperAdmin - Manage system users</p>
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

                                    <!-- Create Admin -->
                                    <div class="content-card" style="margin-bottom: 30px;">
                                        <h4 style="margin-bottom: 20px;"><i class="fas fa-user-shield"
                                                style="color: var(--accent);"></i> Create Admin Account</h4>
                                        <form action="<%= ctx %>/superadmin/users/" method="post">
                                            <input type="hidden" name="action" value="createAdmin">
                                            <div
                                                style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px;">
                                                <div class="form-group">
                                                    <label>Full Name *</label>
                                                    <input type="text" name="fullName" class="form-control-custom"
                                                        required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Username *</label>
                                                    <input type="text" name="username" class="form-control-custom"
                                                        required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Email *</label>
                                                    <input type="email" name="email" class="form-control-custom"
                                                        required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Password *</label>
                                                    <input type="password" name="password" class="form-control-custom"
                                                        required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Phone</label>
                                                    <input type="tel" name="phone" class="form-control-custom">
                                                </div>
                                                <div class="form-group" style="display: flex; align-items: flex-end;">
                                                    <button type="submit" class="btn-primary-custom"
                                                        style="padding: 12px 28px;">
                                                        <i class="fas fa-user-plus"></i> Create Admin
                                                    </button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>

                                    <!-- Users Table -->
                                    <div style="overflow-x: auto;">
                                        <table class="data-table">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th>Username</th>
                                                    <th>Email</th>
                                                    <th>Role</th>
                                                    <th>Verified</th>
                                                    <th>Joined</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% if (users !=null) { for (User u : users) { %>
                                                    <tr>
                                                        <td>
                                                            <%= u.getId() %>
                                                        </td>
                                                        <td><strong>
                                                                <%= u.getFullName() %>
                                                            </strong></td>
                                                        <td>
                                                            <%= u.getUsername() %>
                                                        </td>
                                                        <td>
                                                            <%= u.getEmail() %>
                                                        </td>
                                                        <td>
                                                            <span class="badge-status" style="<%= u.isSuperAdmin() ? "background: #0D1B2A; color: #C6A75E;" :
                                                                u.hasAdminAccess()
                                                                ? "background: #D1ECF1; color: #0C5460;"
                                                                : "background: #E2E3E5; color: #383D41;" %>">
                                                                <%= u.getRole().name() %>
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <%= u.isVerified()
                                                                ? "<i class='fas fa-check-circle' style='color: var(--success);'></i>"
                                                                : "<i class='fas fa-times-circle' style='color: var(--danger);'></i>"
                                                                %>
                                                        </td>
                                                        <td>
                                                            <%= u.getCreatedAt() !=null ?
                                                                u.getCreatedAt().toString().substring(0, 10) : "N/A" %>
                                                        </td>
                                                        <td>
                                                            <% if (!u.isSuperAdmin()) { %>
                                                                <form action="<%= ctx %>/superadmin/users/"
                                                                    method="post" style="display: inline;"
                                                                    onsubmit="return confirmAction('Delete user <%= u.getUsername() %>?');">
                                                                    <input type="hidden" name="action" value="delete">
                                                                    <input type="hidden" name="userId"
                                                                        value="<%= u.getId() %>">
                                                                    <button type="submit" class="btn-dark-custom"
                                                                        style="padding: 6px 12px; font-size: 0.7rem; background: var(--danger);">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </form>
                                                                <% } else { %>
                                                                    <span
                                                                        style="color: var(--text-muted); font-size: 0.8rem;">Protected</span>
                                                                    <% } %>
                                                        </td>
                                                    </tr>
                                                    <% } } %>
                                            </tbody>
                                        </table>
                                    </div>
                </div>

                <jsp:include page="/WEB-INF/views/includes/footer.jsp" />