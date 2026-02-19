<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <jsp:include page="/views/includes/header.jsp">
        <jsp:param name="title" value="Help & FAQ - Ocean View Resort" />
    </jsp:include>

    <div class="page-header">
        <h1><i class="fas fa-life-ring" style="color: var(--accent);"></i> Help & FAQ</h1>
        <p class="breadcrumb-text">Find answers to common questions</p>
    </div>

    <div class="content-wrapper" style="max-width: 800px;">

        <div class="content-card" style="margin-bottom: 30px;">
            <h4 style="margin-bottom: 20px;"><i class="fas fa-question-circle" style="color: var(--accent);"></i>
                Frequently Asked Questions</h4>

            <div class="accordion-item active">
                <div class="accordion-header">
                    How do I make a reservation?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p>To make a reservation, log into your account and navigate to "Book Your Stay."
                        Select your preferred room type, check-in and check-out dates, and fill in guest details.
                        The system will automatically check availability and calculate your total bill including
                        taxes (10%) and service charge (5%).</p>
                </div>
            </div>

            <div class="accordion-item">
                <div class="accordion-header">
                    What is the cancellation policy?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p>Free cancellation is available up to 24 hours before check-in. Late cancellations
                        or no-shows may incur a charge equivalent to one night's stay. To cancel, go to
                        "My Reservations" and click the cancel button on your booking.</p>
                </div>
            </div>

            <div class="accordion-item">
                <div class="accordion-header">
                    How is the bill calculated?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p>Your bill is calculated as: <strong>Number of Nights × Room Rate + 10% Tax + 5% Service
                            Charge</strong>.
                        For example, a 3-night stay in a Deluxe room ($250/night) would be:
                        $750 + $75 (tax) + $37.50 (service) = $862.50 total.</p>
                </div>
            </div>

            <div class="accordion-item">
                <div class="accordion-header">
                    What room types are available?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p><strong>Standard:</strong> $150/night - Comfortable rooms with garden views.<br>
                        <strong>Deluxe:</strong> $250/night - Spacious rooms with partial ocean views.<br>
                        <strong>Suite:</strong> $450/night - Luxury suites with separate living area.<br>
                        <strong>Penthouse:</strong> $800/night - Premium penthouse with rooftop terrace.<br>
                        <strong>Villa:</strong> $1,200/night - Private villa with pool and full ocean view.
                    </p>
                </div>
            </div>

            <div class="accordion-item">
                <div class="accordion-header">
                    What are the check-in and check-out times?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p>Check-in time is <strong>2:00 PM</strong> and check-out time is <strong>12:00 PM (noon)</strong>.
                        Early check-in or late check-out may be available upon request, subject to availability.</p>
                </div>
            </div>

            <div class="accordion-item">
                <div class="accordion-header">
                    How do I reset my password?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p>Click "Forgot Password" on the login page and enter your registered email address.
                        You'll receive a password reset link. Click the link and set a new password that meets
                        our security requirements (8+ characters, uppercase, lowercase, number, and special character).
                    </p>
                </div>
            </div>

            <div class="accordion-item">
                <div class="accordion-header">
                    I didn't receive the verification email. What should I do?
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="accordion-body">
                    <p>Check your spam/junk folder. The email is sent from our oceanviewresort.lk domain.
                        If you still can't find it, please contact our front desk at +94 11 234 5678
                        or email info@oceanviewresort.lk for manual account verification.</p>
                </div>
            </div>
        </div>

        <!-- Contact Card -->
        <div class="content-card"
            style="background: linear-gradient(135deg, var(--primary), var(--primary-light)); color: var(--text-light);">
            <h4 style="color: var(--accent); margin-bottom: 16px;"><i class="fas fa-headset"></i> Need More Help?</h4>
            <p style="color: rgba(255,255,255,0.8); margin-bottom: 16px;">Our dedicated support team is available 24/7
                to assist you.</p>
            <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 16px;">
                <div>
                    <i class="fas fa-phone" style="color: var(--accent); font-size: 1.2rem;"></i>
                    <p style="font-size: 0.9rem; margin-top: 4px;">+94 11 234 5678</p>
                </div>
                <div>
                    <i class="fas fa-envelope" style="color: var(--accent); font-size: 1.2rem;"></i>
                    <p style="font-size: 0.9rem; margin-top: 4px;">info@oceanviewresort.lk</p>
                </div>
                <div>
                    <i class="fas fa-comments" style="color: var(--accent); font-size: 1.2rem;"></i>
                    <p style="font-size: 0.9rem; margin-top: 4px;">Live Chat Available</p>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/views/includes/footer.jsp" />