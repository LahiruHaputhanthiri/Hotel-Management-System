<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.oceanview.model.User" %>
        <% User dashUser=(User) session.getAttribute("user"); String ctx=request.getContextPath(); int
            totalReservations=(Integer) request.getAttribute("totalReservations"); double totalRevenue=(Double)
            request.getAttribute("totalRevenue"); int activeUsers=(Integer) request.getAttribute("activeUsers"); double
            occupancyRate=(Double) request.getAttribute("occupancyRate"); String topRoomType=(String)
            request.getAttribute("topRoomType"); String monthlyRevenueJson=(String)
            request.getAttribute("monthlyRevenueJson"); String statusCountsJson=(String)
            request.getAttribute("statusCountsJson"); %>
            <jsp:include page="/views/includes/header.jsp">
                <jsp:param name="title" value="Admin Dashboard - Ocean View Resort" />
            </jsp:include>

            <div class="page-header">
                <h1><i class="fas fa-tachometer-alt" style="color: var(--accent);"></i> Admin Dashboard</h1>
                <p class="breadcrumb-text">Welcome back, <%= dashUser.getFullName() %>
                </p>
            </div>

            <div class="content-wrapper">

                <!-- Stats Grid -->
                <div
                    style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 24px; margin-bottom: 36px;">

                    <div class="stat-card animate-fadeInUp">
                        <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
                        <div class="stat-value">
                            <%= totalReservations %>
                        </div>
                        <div class="stat-label">Total Reservations</div>
                    </div>

                    <div class="stat-card animate-fadeInUp delay-1">
                        <div class="stat-icon"><i class="fas fa-dollar-sign"></i></div>
                        <div class="stat-value">$<%= String.format("%,.0f", totalRevenue) %>
                        </div>
                        <div class="stat-label">Total Revenue</div>
                    </div>

                    <div class="stat-card animate-fadeInUp delay-2">
                        <div class="stat-icon"><i class="fas fa-users"></i></div>
                        <div class="stat-value">
                            <%= activeUsers %>
                        </div>
                        <div class="stat-label">Active Users</div>
                    </div>

                    <div class="stat-card animate-fadeInUp delay-3">
                        <div class="stat-icon"><i class="fas fa-chart-pie"></i></div>
                        <div class="stat-value">
                            <%= String.format("%.0f", occupancyRate) %>%
                        </div>
                        <div class="stat-label">Occupancy Rate</div>
                    </div>

                </div>

                <!-- Top Room & Quick Actions -->
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 36px;">
                    <div class="content-card">
                        <h4><i class="fas fa-trophy" style="color: var(--accent);"></i> Most Popular Room</h4>
                        <p style="font-size: 1.5rem; font-weight: 700; color: var(--primary); margin-top: 12px;">
                            <%= topRoomType !=null ? topRoomType : "N/A" %>
                        </p>
                    </div>
                    <div class="content-card">
                        <h4><i class="fas fa-bolt" style="color: var(--accent);"></i> Quick Actions</h4>
                        <div style="display: flex; gap: 12px; margin-top: 12px; flex-wrap: wrap;">
                            <a href="<%= ctx %>/reservations/" class="btn-dark-custom"
                                style="padding: 10px 20px; font-size: 0.75rem;">
                                <i class="fas fa-list"></i> All Bookings
                            </a>
                            <a href="<%= ctx %>/admin/rooms/" class="btn-dark-custom"
                                style="padding: 10px 20px; font-size: 0.75rem;">
                                <i class="fas fa-bed"></i> Manage Rooms
                            </a>
                            <a href="<%= ctx %>/admin/export/reservations" class="btn-primary-custom"
                                style="padding: 10px 20px; font-size: 0.75rem;">
                                <i class="fas fa-file-excel"></i> Export Reports
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Charts -->
                <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 24px;">
                    <div class="chart-container">
                        <h4 style="margin-bottom: 20px;"><i class="fas fa-chart-line" style="color: var(--accent);"></i>
                            Monthly Revenue</h4>
                        <canvas id="revenueChart" height="250"></canvas>
                    </div>
                    <div class="chart-container">
                        <h4 style="margin-bottom: 20px;"><i class="fas fa-chart-doughnut"
                                style="color: var(--accent);"></i> Booking Status</h4>
                        <canvas id="statusChart" height="250"></canvas>
                    </div>
                </div>

            </div>

            <!-- Chart.js -->
            <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
            <script>
                // Monthly Revenue Chart
                var monthlyData = JSON.parse('<%= monthlyRevenueJson %>');
                var labels = Object.keys(monthlyData);
                var values = Object.values(monthlyData);

                new Chart(document.getElementById('revenueChart'), {
                    type: 'bar',
                    data: {
                        labels: labels.length > 0 ? labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                        datasets: [{
                            label: 'Revenue ($)',
                            data: values.length > 0 ? values : [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                            backgroundColor: 'rgba(198, 167, 94, 0.3)',
                            borderColor: '#C6A75E',
                            borderWidth: 2,
                            borderRadius: 6
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: { legend: { display: false } },
                        scales: {
                            y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.05)' } },
                            x: { grid: { display: false } }
                        }
                    }
                });

                // Status Doughnut Chart
                var statusData = JSON.parse('<%= statusCountsJson %>');
                var statusLabels = Object.keys(statusData);
                var statusValues = Object.values(statusData);
                var statusColors = ['#F39C12', '#2ECC71', '#3498DB', '#95A5A6', '#E74C3C'];

                new Chart(document.getElementById('statusChart'), {
                    type: 'doughnut',
                    data: {
                        labels: statusLabels.length > 0 ? statusLabels : ['No Data'],
                        datasets: [{
                            data: statusValues.length > 0 ? statusValues : [1],
                            backgroundColor: statusLabels.length > 0 ? statusColors.slice(0, statusLabels.length) : ['#E8E8E8'],
                            borderWidth: 0
                        }]
                    },
                    options: {
                        responsive: true,
                        cutout: '65%',
                        plugins: { legend: { position: 'bottom', labels: { padding: 15 } } }
                    }
                });
            </script>

            <jsp:include page="/views/includes/footer.jsp" />