package com.oceanview.controller;

import com.oceanview.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/diagnostics")
public class DiagnosticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Ocean View Resort - Diagnostics</h1>");

        // 1. Check Database
        out.println("<h2>1. Database Connectivity</h2>");
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            if (conn != null && !conn.isClosed()) {
                out.println("<p style='color:green'>SUCCESS: Connected to database.</p>");
            } else {
                out.println("<p style='color:red'>FAILED: Connection is null or closed.</p>");
            }
        } catch (Exception e) {
            out.println("<p style='color:red'>ERROR: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }

        // 2. Check Mail Libraries
        out.println("<h2>2. Mail Library Check</h2>");

        out.println("<p><b>Jakarta Mail:</b> ");
        try {
            Class.forName("jakarta.mail.Session");
            out.println("<span style='color:green'>FOUND (Correct for Jakarta EE 10)</span>");
        } catch (ClassNotFoundException e) {
            out.println("<span style='color:red'>NOT FOUND</span>");
        }
        out.println("</p>");

        out.println("<p><b>Javax Mail:</b> ");
        try {
            Class.forName("javax.mail.Session");
            out.println("<span style='color:orange'>FOUND (Legacy - May cause conflicts)</span>");
        } catch (ClassNotFoundException e) {
            out.println("<span style='color:gray'>NOT FOUND</span>");
        }
        out.println("</p>");

        // 3. Session Info
        out.println("<h2>3. Session Info</h2>");
        Object user = request.getSession().getAttribute("user");
        out.println("<p>Logged in User: " + (user != null ? user.toString() : "NONE") + "</p>");

        out.println("<br><a href='" + request.getContextPath() + "/'>Back to Home</a>");
        out.println("</body></html>");
    }
}
