<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.oceanview.util.CSRFTokenUtil" %>
        <% String ctx=request.getContextPath(); String error=(String) request.getAttribute("error"); String
            resetToken=(String) request.getAttribute("resetToken"); String csrfToken=(String)
            request.getAttribute("csrfToken"); if (csrfToken==null)
            csrfToken=com.oceanview.util.CSRFTokenUtil.generateToken(request); %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Reset Password - Ocean View Resort</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="<%= ctx %>/css/style.css">
            </head>

            <body>
                <div class="auth-wrapper">
                    <div class="auth-card animate-fadeInUp" style="max-width: 440px;">
                        <div class="auth-header">
                            <div class="brand-name">Ocean View</div>
                            <div class="brand-sub">Resort & Spa</div>
                            <h3 style="margin-top: 24px;">Reset Password</h3>
                        </div>

                        <% if (error !=null) { %>
                            <div class="alert-custom alert-danger"><i class="fas fa-exclamation-circle"></i>
                                <%= error %>
                            </div>
                            <% } %>

                                <form action="<%= ctx %>/auth" method="post">
                                    <input type="hidden" name="action" value="resetPassword">
                                    <input type="hidden" name="token" value="<%= resetToken %>">
                                    <input type="hidden" name="csrfToken" value="<%= csrfToken %>">

                                    <div class="form-group">
                                        <label>New Password</label>
                                        <input type="password" name="newPassword" class="form-control-custom"
                                            placeholder="Min 8 characters" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Confirm Password</label>
                                        <input type="password" name="confirmPassword" class="form-control-custom"
                                            placeholder="Repeat password" required>
                                    </div>
                                    <button type="submit" class="btn-primary-custom"
                                        style="width: 100%; text-align: center;">
                                        <i class="fas fa-key"></i> Reset Password
                                    </button>
                                </form>
                    </div>
                </div>
            </body>

            </html>