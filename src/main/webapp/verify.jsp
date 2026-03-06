<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% String ctx=request.getContextPath(); String email=request.getParameter("email"); String
        success=request.getParameter("success"); String error=request.getParameter("error"); %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Verify Email - Ocean View Resort</title>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="<%= ctx %>/css/style.css">
            <style>
                .verify-card {
                    max-width: 500px;
                    margin: 100px auto;
                    text-align: center;
                    padding: 40px;
                    background: rgba(255, 255, 255, 0.05);
                    backdrop-filter: blur(20px);
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    border-radius: 20px;
                    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
                }

                .icon-box {
                    width: 80px;
                    height: 80px;
                    background: var(--accent-gradient);
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    margin: 0 auto 24px;
                    font-size: 32px;
                    color: white;
                    box-shadow: 0 10px 20px rgba(198, 167, 94, 0.3);
                }

                .icon-success {
                    background: linear-gradient(135deg, #28a745, #20c997);
                }
            </style>
        </head>

        <body>
            <div class="auth-wrapper">
                <div class="verify-card animate-fadeInUp">
                    <% if (success !=null) { %>
                        <div class="icon-box icon-success animate-scaleIn">
                            <i class="fas fa-check"></i>
                        </div>
                        <h2 class="mb-3">Account Verified!</h2>
                        <p class="text-muted mb-4">Your email has been successfully verified. You can now access all
                            features of Ocean View Resort.</p>
                        <a href="<%= ctx %>/login.jsp" class="btn-primary-custom w-100 py-3">
                            <i class="fas fa-sign-in-alt mr-2"></i> Proceed to Login
                        </a>
                        <% } else if (error !=null) { %>
                            <div class="icon-box" style="background: linear-gradient(135deg, #dc3545, #f86c6b);">
                                <i class="fas fa-exclamation-triangle"></i>
                            </div>
                            <h2 class="mb-3">Verification Failed</h2>
                            <p class="text-muted mb-4">
                                <%= error %>
                            </p>
                            <a href="<%= ctx %>/contact" class="btn-secondary-custom w-100 py-3 mb-3">Contact
                                Support</a>
                            <a href="<%= ctx %>/register.jsp" class="btn-link-custom">Back to Registration</a>
                            <% } else { %>
                                <div class="icon-box animate-pulse">
                                    <i class="fas fa-envelope-open-text"></i>
                                </div>
                                <h2 class="mb-3">Check Your Email</h2>
                                <p class="text-muted mb-4">
                                    We've sent a verification link to <strong>
                                        <%= (email !=null ? email : "your email address" ) %>
                                    </strong>.
                                    Please click the link to activate your account.
                                </p>
                                <div class="alert-custom alert-info mb-4">
                                    <i class="fas fa-info-circle"></i> Don't see it? Check your spam folder.
                                </div>
                                <p class="small text-muted mb-0">
                                    Did not receive the email?
                                    <a href="<%= ctx %>/auth?action=resendVerification&email=<%= email %>"
                                        class="btn-link-custom">Resend Verification</a>
                                </p>
                                <% } %>
                </div>
            </div>
        </body>

        </html>