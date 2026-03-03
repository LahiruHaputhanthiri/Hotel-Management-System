<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.oceanview.util.CSRFTokenUtil" %>
        <% String ctx=request.getContextPath(); String error=(String) request.getAttribute("error"); String
            success=(String) request.getAttribute("success"); String csrfToken=(String)
            request.getAttribute("csrfToken"); if (csrfToken==null)
            csrfToken=com.oceanview.util.CSRFTokenUtil.generateToken(request); String msg=request.getParameter("msg");
            %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Sign In - Ocean View Resort</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="<%= ctx %>/css/style.css">
            </head>

            <body>

                <div class="auth-wrapper">
                    <div class="auth-card animate-fadeInUp">

                        <div class="auth-header">
                            <a href="<%= ctx %>/" style="text-decoration: none;">
                                <div class="brand-name">Ocean View</div>
                                <div class="brand-sub">Resort & Spa</div>
                            </a>
                            <h3>Welcome Back</h3>
                            <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 4px;">Sign in to manage
                                your
                                reservations</p>
                        </div>

                        <% if ("loggedOut".equals(msg)) { %>
                            <div class="alert-custom alert-info" data-dismiss><i class="fas fa-info-circle"></i> You
                                have
                                been logged out.</div>
                            <% } %>
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
                                                    <input type="hidden" name="action" value="login">
                                                    <input type="hidden" name="csrfToken" value="<%= csrfToken %>">

                                                    <div class="form-group">
                                                        <label><i class="fas fa-user"
                                                                style="color: var(--accent); margin-right: 6px;"></i>Username
                                                            or Email</label>
                                                        <input type="text" name="usernameOrEmail"
                                                            class="form-control-custom"
                                                            placeholder="Enter your username or email" required
                                                            autofocus>
                                                    </div>

                                                    <div class="form-group">
                                                        <label><i class="fas fa-lock"
                                                                style="color: var(--accent); margin-right: 6px;"></i>Password</label>
                                                        <input type="password" name="password"
                                                            class="form-control-custom"
                                                            placeholder="Enter your password" required>
                                                    </div>

                                                    <div
                                                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
                                                        <label
                                                            style="display: flex; align-items: center; gap: 6px; font-size: 0.85rem; cursor: pointer;">
                                                            <input type="checkbox" name="rememberMe"
                                                                style="accent-color: var(--accent);"> Remember me
                                                        </label>
                                                        <a href="<%= ctx %>/forgot_password.jsp"
                                                            style="font-size: 0.85rem;">Forgot Password?</a>
                                                    </div>

                                                    <button type="submit" class="btn-primary-custom"
                                                        style="width: 100%; text-align: center;">
                                                        <i class="fas fa-sign-in-alt"></i> Sign In
                                                    </button>
                                                </form>

                                                <div
                                                    style="text-align: center; margin-top: 28px; padding-top: 20px; border-top: 1px solid var(--border);">
                                                    <p style="color: var(--text-muted); font-size: 0.9rem;">
                                                        Don't have an account?
                                                        <a href="<%= ctx %>/register.jsp"
                                                            style="font-weight: 600;">Create
                                                            Account</a>
                                                    </p>
                                                </div>
                    </div>
                </div>

                <script src="<%= ctx %>/js/app.js"></script>
            </body>

            </html>