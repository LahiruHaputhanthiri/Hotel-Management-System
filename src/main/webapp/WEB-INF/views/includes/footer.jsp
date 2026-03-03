<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% String ctxF=request.getContextPath(); %>

        <!-- Footer -->
        <footer class="footer">
            <div class="footer-grid">
                <div>
                    <h4>Ocean View Resort</h4>
                    <p>Experience luxury and tranquility at our beachfront resort.
                        Where the ocean meets elegance, creating unforgettable memories
                        for every guest.</p>
                    <div style="margin-top: 20px;">
                        <a href="#" style="color: var(--accent); margin-right: 16px; font-size: 1.2rem;"><i
                                class="fab fa-facebook-f"></i></a>
                        <a href="#" style="color: var(--accent); margin-right: 16px; font-size: 1.2rem;"><i
                                class="fab fa-instagram"></i></a>
                        <a href="#" style="color: var(--accent); margin-right: 16px; font-size: 1.2rem;"><i
                                class="fab fa-twitter"></i></a>
                        <a href="#" style="color: var(--accent); font-size: 1.2rem;"><i class="fab fa-youtube"></i></a>
                    </div>
                </div>
                <div>
                    <h4>Quick Links</h4>
                    <ul class="footer-links">
                        <li><a href="<%= ctxF %>/">Home</a></li>
                        <li><a href="<%= ctxF %>/login.jsp">Sign In</a></li>
                        <li><a href="<%= ctxF %>/register.jsp">Register</a></li>
                        <li><a href="<%= ctxF %>/help">Help & FAQ</a></li>
                    </ul>
                </div>
                <div>
                    <h4>Services</h4>
                    <ul class="footer-links">
                        <li><a href="#">Room Reservations</a></li>
                        <li><a href="#">Fine Dining</a></li>
                        <li><a href="#">Ocean Spa</a></li>
                        <li><a href="#">Events & Conferences</a></li>
                    </ul>
                </div>
                <div>
                    <h4>Contact</h4>
                    <ul class="footer-links">
                        <li><i class="fas fa-map-marker-alt" style="color: var(--accent); width: 20px;"></i> Galle Road,
                            Colombo 3, Sri Lanka</li>
                        <li><i class="fas fa-phone" style="color: var(--accent); width: 20px;"></i> +94 11 234 5678</li>
                        <li><i class="fas fa-envelope" style="color: var(--accent); width: 20px;"></i>
                            info@oceanviewresort.lk</li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2025 Ocean View Resort & Spa. All Rights Reserved. | Designed with <i class="fas fa-heart"
                        style="color: var(--accent);"></i></p>
            </div>
        </footer>

        <!-- Dark Mode Toggle -->
        <button class="dark-mode-toggle" onclick="toggleDarkMode()" title="Toggle Dark Mode">
            <i class="fas fa-moon" id="darkModeIcon"></i>
        </button>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <!-- Custom JS -->
        <script src="<%= ctxF %>/js/app.js"></script>

        </body>

        </html>