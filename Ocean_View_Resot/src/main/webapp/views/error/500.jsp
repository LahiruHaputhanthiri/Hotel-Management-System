<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>500 - Server Error</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    </head>

    <body>
        <div class="auth-wrapper" style="text-align: center;">
            <div class="auth-card" style="max-width: 500px;">
                <i class="fas fa-exclamation-triangle"
                    style="font-size: 4rem; color: var(--accent); margin-bottom: 20px;"></i>
                <h2 style="margin-bottom: 8px;">500 - Server Error</h2>
                <p style="color: var(--text-muted); margin-bottom: 24px;">Something went wrong on our end. Please try
                    again later.</p>
                <a href="<%= request.getContextPath() %>/" class="btn-primary-custom"><i class="fas fa-home"></i> Go
                    Home</a>
            </div>
        </div>
    </body>

    </html>