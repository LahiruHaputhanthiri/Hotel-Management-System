<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% String ctx=request.getContextPath(); String error=(String) request.getAttribute("error"); String success=(String)
        request.getAttribute("success"); %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Forgot Password - Ocean View Resort</title>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="<%= ctx %>/css/style.css">
        </head>

        <body>
            <div class="auth-wrapper">
                <div class="auth-card animate-fadeInUp" style="max-width: 440px;">
                    <div class="auth-header">
                        <a href="<%= ctx %>/" style="text-decoration: none;">
                            <div class="brand-name">Ocean View</div>
                            <div class="brand-sub">Resort & Spa</div>
                        </a>
                        <h3 style="margin-top: 24px;">Forgot Password</h3>
                        <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 4px;">Enter your email to
                            receive a reset link</p>
                    </div>

                    <% if (error !=null) { %>
                        <div class="alert-custom alert-danger"><i class="fas fa-exclamation-circle"></i>
                            <%= error %>
                        </div>
                        <% } %>
                            <% if (success !=null) { %>
                                <div class="alert-custom alert-success"><i class="fas fa-check-circle"></i>
                                    <%= success %>
                                </div>
                                <% } %>

                                    <form action="<%= ctx %>/auth" method="post">
                                        <input type="hidden" name="action" value="forgotPassword">
                                        <div class="form-group">
                                            <label>Email Address</label>
                                            <input type="email" name="email" class="form-control-custom"
                                                placeholder="Enter your registered email" required autofocus>
                                        </div>
                                        <button type="submit" class="btn-primary-custom"
                                            style="width: 100%; text-align: center;">
                                            <i class="fas fa-paper-plane"></i> Send Reset Link
                                        </button>
                                    </form>

                                    <div style="text-align: center; margin-top: 24px;">
                                        <a href="<%= ctx %>/login.jsp" style="font-size: 0.9rem;"><i
                                                class="fas fa-arrow-left"></i> Back to Sign In</a>
                                    </div>
                </div>
            </div>
        </body>

        </html>