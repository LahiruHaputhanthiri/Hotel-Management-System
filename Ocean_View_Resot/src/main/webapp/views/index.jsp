<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <jsp:include page="/views/includes/header.jsp">
        <jsp:param name="title" value="Ocean View Resort - Luxury Beachfront Hotel" />
    </jsp:include>

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-bg"></div>
        <div class="hero-overlay"></div>
        <div class="hero-content">
            <p class="subtitle">Welcome to Paradise</p>
            <h1>Where the Ocean Meets Elegance</h1>
            <p>Nestled along the pristine coastline, Ocean View Resort offers an unparalleled
                luxury experience with breathtaking ocean views, world-class dining, and
                rejuvenating spa treatments.</p>
            <div style="display: flex; gap: 16px; justify-content: center; flex-wrap: wrap;">
                <a href="<%= request.getContextPath() %>/reservations/new" class="btn-primary-custom">Book Your Stay</a>
                <a href="#rooms" class="btn-outline-custom">Explore Rooms</a>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="section" style="background: var(--bg-cream);">
        <div class="content-wrapper">
            <div class="section-header">
                <span class="section-label">Why Choose Us</span>
                <h2>A World of Luxury Awaits</h2>
                <div class="divider"></div>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 30px;">
                <div class="stat-card animate-on-scroll" style="text-align: center;">
                    <div class="stat-icon" style="margin: 0 auto 16px;">
                        <i class="fas fa-water"></i>
                    </div>
                    <h4>Ocean Views</h4>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 8px;">
                        Every room offers stunning panoramic views of the Indian Ocean.
                    </p>
                </div>
                <div class="stat-card animate-on-scroll" style="text-align: center;">
                    <div class="stat-icon" style="margin: 0 auto 16px;">
                        <i class="fas fa-utensils"></i>
                    </div>
                    <h4>Fine Dining</h4>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 8px;">
                        Award-winning restaurants featuring local and international cuisine.
                    </p>
                </div>
                <div class="stat-card animate-on-scroll" style="text-align: center;">
                    <div class="stat-icon" style="margin: 0 auto 16px;">
                        <i class="fas fa-spa"></i>
                    </div>
                    <h4>Luxury Spa</h4>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 8px;">
                        Rejuvenate with Ayurvedic treatments and modern wellness therapies.
                    </p>
                </div>
                <div class="stat-card animate-on-scroll" style="text-align: center;">
                    <div class="stat-icon" style="margin: 0 auto 16px;">
                        <i class="fas fa-concierge-bell"></i>
                    </div>
                    <h4>Premium Service</h4>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 8px;">
                        24/7 concierge and personalized butler service for every guest.
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- Room Types Section -->
    <section class="section" id="rooms">
        <div class="content-wrapper">
            <div class="section-header">
                <span class="section-label">Accommodations</span>
                <h2>Our Room Collection</h2>
                <p>Each room is thoughtfully designed to provide comfort, elegance, and unforgettable ocean views.</p>
                <div class="divider"></div>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 30px;">

                <!-- Standard Room -->
                <div class="room-card animate-on-scroll">
                    <div class="card-image">
                        <img src="<%= request.getContextPath() %>/images/room_standard.jpg" alt="Standard Room">
                        <span class="price-tag">$150/night</span>
                    </div>
                    <div class="card-body">
                        <h4>Standard Room</h4>
                        <p>Comfortable and well-appointed with modern amenities and garden views.</p>
                    </div>
                    <div class="card-footer">
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><i class="fas fa-user"></i> 2
                            Guests</span>
                        <a href="<%= request.getContextPath() %>/reservations/new" class="btn-dark-custom"
                            style="padding: 8px 20px; font-size: 0.75rem;">Book Now</a>
                    </div>
                </div>

                <!-- Deluxe Room -->
                <div class="room-card animate-on-scroll">
                    <div class="card-image">
                        <img src="<%= request.getContextPath() %>/images/room_deluxe.jpg" alt="Deluxe Room">
                        <span class="price-tag">$250/night</span>
                    </div>
                    <div class="card-body">
                        <h4>Deluxe Room</h4>
                        <p>Spacious interiors with premium furnishings and partial ocean views.</p>
                    </div>
                    <div class="card-footer">
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><i class="fas fa-user"></i> 3
                            Guests</span>
                        <a href="<%= request.getContextPath() %>/reservations/new" class="btn-dark-custom"
                            style="padding: 8px 20px; font-size: 0.75rem;">Book Now</a>
                    </div>
                </div>

                <!-- Suite -->
                <div class="room-card animate-on-scroll">
                    <div class="card-image">
                        <img src="<%= request.getContextPath() %>/images/room_suite.jpg" alt="Suite">
                        <span class="price-tag">$450/night</span>
                    </div>
                    <div class="card-body">
                        <h4>Executive Suite</h4>
                        <p>Luxurious suite with separate living area and panoramic ocean views.</p>
                    </div>
                    <div class="card-footer">
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><i class="fas fa-user"></i> 4
                            Guests</span>
                        <a href="<%= request.getContextPath() %>/reservations/new" class="btn-dark-custom"
                            style="padding: 8px 20px; font-size: 0.75rem;">Book Now</a>
                    </div>
                </div>

                <!-- Penthouse -->
                <div class="room-card animate-on-scroll">
                    <div class="card-image">
                        <img src="<%= request.getContextPath() %>/images/room_penthouse.jpg" alt="Penthouse">
                        <span class="price-tag">$800/night</span>
                    </div>
                    <div class="card-body">
                        <h4>Rooftop Penthouse</h4>
                        <p>Modern rooftop living with 360-degree views and private terrace.</p>
                    </div>
                    <div class="card-footer">
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><i class="fas fa-user"></i> 4
                            Guests</span>
                        <a href="<%= request.getContextPath() %>/reservations/new" class="btn-dark-custom"
                            style="padding: 8px 20px; font-size: 0.75rem;">Book Now</a>
                    </div>
                </div>

                <!-- Villa -->
                <div class="room-card animate-on-scroll">
                    <div class="card-image">
                        <img src="<%= request.getContextPath() %>/images/room_villa.jpg" alt="Villa">
                        <span class="price-tag">$1200/night</span>
                    </div>
                    <div class="card-body">
                        <h4>Beachfront Villa</h4>
                        <p>Ultimate privacy with direct beach access and private swimming pool.</p>
                    </div>
                    <div class="card-footer">
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><i class="fas fa-user"></i> 6
                            Guests</span>
                        <a href="<%= request.getContextPath() %>/reservations/new" class="btn-dark-custom"
                            style="padding: 8px 20px; font-size: 0.75rem;">Book Now</a>
                    </div>
                </div>

            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="section"
        style="background: linear-gradient(135deg, var(--primary), var(--primary-light)); text-align: center; padding: 80px 0;">
        <div class="content-wrapper">
            <span class="section-label" style="color: var(--accent); display: block; margin-bottom: 12px;">Ready to
                Experience Luxury?</span>
            <h2 style="color: var(--text-light); font-size: 2.5rem; margin-bottom: 16px;">Book Your Dream Getaway Today
            </h2>
            <p style="color: rgba(255,255,255,0.7); max-width: 600px; margin: 0 auto 32px;">
                Special rates available for extended stays. Reserve now and let us create
                an unforgettable experience for you.
            </p>
            <a href="<%= request.getContextPath() %>/register.jsp" class="btn-primary-custom">Get Started</a>
        </div>
    </section>

    <jsp:include page="/views/includes/footer.jsp" />