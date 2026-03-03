<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.oceanview.model.User" %>
        <% User navUser=(User) session.getAttribute("user"); String ctx=request.getContextPath(); %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="description"
                    content="Ocean View Resort - A luxury beachfront hotel offering premium rooms, dining, and spa services.">
                <title>${param.title != null ? param.title : 'Ocean View Resort'}</title>

                <!-- Fonts & Icons -->
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

                <!-- Bootstrap 5 -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

                <!-- Custom CSS -->
                <link rel="stylesheet" href="<%= ctx %>/css/style.css">
            </head>

            <body>

                <!-- Navbar -->
                <nav class="navbar" id="mainNav">
                    <div class="container">
                        <a href="<%= ctx %>/" class="navbar-brand">
                            <div class="logo-icon">
                                <i class="fas fa-umbrella-beach"></i>
                            </div>
                            <div>
                                <span class="logo-text">Ocean View</span>
                                <span class="logo-sub">Resort & Spa</span>
                            </div>
                        </a>

                        <div class="nav-toggle" onclick="toggleMobileNav()">
                            <span></span><span></span><span></span>
                        </div>

                        <ul class="nav-links" id="navLinks">
                            <li><a href="<%= ctx %>/">Home</a></li>
                            <% if (navUser !=null) { %>
                                <% if (navUser.hasAdminAccess()) { %>
                                    <li><a href="<%= ctx %>/admin/dashboard">Dashboard</a></li>
                                    <li><a href="<%= ctx %>/reservations/">Reservations</a></li>
                                    <li><a href="<%= ctx %>/admin/rooms/">Rooms</a></li>
                                    <% if (navUser.isSuperAdmin()) { %>
                                        <li><a href="<%= ctx %>/superadmin/users/">Users</a></li>
                                        <% } %>
                                            <% } else { %>
                                                <li><a href="<%= ctx %>/user/dashboard">My Dashboard</a></li>
                                                <li><a href="<%= ctx %>/reservations/my">My Bookings</a></li>
                                                <% } %>
                                                    <li><a href="<%= ctx %>/help">Help</a></li>
                                                    <li>
                                                        <a href="<%= ctx %>/auth?action=logout" class="nav-btn"
                                                            title="Signed in as <%= navUser.getUsername() %>">
                                                            <i class="fas fa-sign-out-alt"></i> Logout
                                                        </a>
                                                    </li>
                                                    <% } else { %>
                                                        <li><a href="<%= ctx %>/help">Help</a></li>
                                                        <li><a href="<%= ctx %>/login.jsp" class="nav-btn">Sign In</a>
                                                        </li>
                                                        <% } %>
                        </ul>
                    </div>
                </nav>