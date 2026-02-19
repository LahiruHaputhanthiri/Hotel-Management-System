<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% String ctx=request.getContextPath(); String error=(String) request.getAttribute("error"); String
        csrfToken=(String) request.getAttribute("csrfToken"); if (csrfToken==null)
        csrfToken=com.oceanview.util.CSRFTokenUtil.generateToken(request); String regUsername=(String)
        request.getAttribute("regUsername"); String regEmail=(String) request.getAttribute("regEmail"); String
        regFullName=(String) request.getAttribute("regFullName"); String regPhone=(String)
        request.getAttribute("regPhone"); %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Create Account - Ocean View Resort</title>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="<%= ctx %>/css/style.css">
        </head>

        <body>

            <div class="auth-wrapper">
                <div class="auth-card animate-fadeInUp" style="max-width: 520px;">

                    <div class="auth-header">
                        <a href="<%= ctx %>/" style="text-decoration: none;">
                            <div class="brand-name">Ocean View</div>
                            <div class="brand-sub">Resort & Spa</div>
                        </a>
                        <h3>Create Your Account</h3>
                        <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 4px;">Join us for an
                            exclusive experience</p>
                    </div>

                    <% if (error !=null) { %>
                        <div class="alert-custom alert-danger"><i class="fas fa-exclamation-circle"></i>
                            <%= error %>
                        </div>
                        <% } %>

                            <form action="<%= ctx %>/auth" method="post">
                                <input type="hidden" name="action" value="register">
                                <input type="hidden" name="csrfToken" value="<%= csrfToken %>">

                                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                                    <div class="form-group">
                                        <label>Full Name *</label>
                                        <input type="text" name="fullName" class="form-control-custom"
                                            value="<%= regFullName != null ? regFullName : "" %>" placeholder="John Doe"
                                            required>
                                    </div>
                                    <div class="form-group">
                                        <label>Username *</label>
                                        <input type="text" name="username" class="form-control-custom"
                                            value="<%= regUsername != null ? regUsername : "" %>" placeholder="johndoe"
                                            required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Email Address *</label>
                                    <input type="email" name="email" class="form-control-custom"
                                        value="<%= regEmail != null ? regEmail : "" %>" placeholder="john@example.com"
                                        required>
                                </div>

                                <div class="form-group">
                                    <label>Phone Number</label>
                                    <input type="tel" name="phone" class="form-control-custom"
                                        value="<%= regPhone != null ? regPhone : "" %>" placeholder="+94 77 123 4567">
                                </div>

                                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                                    <div class="form-group">
                                        <label>Password *</label>
                                        <input type="password" name="password" class="form-control-custom"
                                            placeholder="Min 8 characters" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Confirm Password *</label>
                                        <input type="password" name="confirmPassword" class="form-control-custom"
                                            placeholder="Repeat password" required>
                                    </div>
                                </div>

                                <p style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 20px;">
                                    <i class="fas fa-info-circle" style="color: var(--accent);"></i>
                                    Password must be 8+ characters with uppercase, lowercase, number, and special
                                    character.
                                </p>

                                <button type="submit" class="btn-primary-custom"
                                    style="width: 100%; text-align: center;">
                                    <i class="fas fa-user-plus"></i> Create Account
                                </button>
                            </form>

                            <div
                                style="text-align: center; margin-top: 28px; padding-top: 20px; border-top: 1px solid var(--border);">
                                <p style="color: var(--text-muted); font-size: 0.9rem;">
                                    Already have an account?
                                    <a href="<%= ctx %>/login.jsp" style="font-weight: 600;">Sign In</a>
                                </p>
                            </div>
                </div>
            </div>

            <script src="<%= ctx %>/js/app.js"></script>
        </body>

        </html>