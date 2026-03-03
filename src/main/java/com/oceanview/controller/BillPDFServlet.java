package com.oceanview.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to generate a PDF bill for a reservation.
 */
@WebServlet("/reservations/bill/pdf")
public class BillPDFServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resNum = request.getParameter("resNum");
        if (resNum == null || resNum.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing reservation number");
            return;
        }

        Reservation res = reservationDAO.findByReservationNumber(resNum);
        if (res == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reservation not found");
            return;
        }

        Payment payment = paymentDAO.findByReservation(res.getId());

        // Prepare response
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Invoice_" + resNum + ".pdf");

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Professional Look & Feel
            BaseColor goldColor = new BaseColor(198, 167, 94); // #C6A75E
            BaseColor navyColor = new BaseColor(13, 27, 42); // #0D1B2A

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, navyColor);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            // Header Section
            Paragraph header = new Paragraph("OCEAN VIEW RESORT", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph subHeader = new Paragraph("Luxury Waterfront Living", subTitleFont);
            subHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(subHeader);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "----------------------------------------------------------------------------------------------------------------------------------",
                    FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.LIGHT_GRAY)));
            document.add(new Paragraph(" "));

            // Invoice Info Table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            // infoTable.setBorderWidth(0); // This method doesn't exist on PdfPTable

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.addElement(new Phrase("Invoiced To:", boldFont));
            leftCell.addElement(new Phrase(res.getGuestName(), bodyFont));
            leftCell.addElement(new Phrase(res.getAddress(), bodyFont));
            leftCell.addElement(new Phrase(res.getContactNumber(), bodyFont));
            infoTable.addCell(leftCell);

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(new Phrase("Invoice Number: " + res.getReservationNumber(), boldFont));
            rightCell.addElement(new Phrase("Date: " + new java.util.Date().toString(), bodyFont));
            rightCell.addElement(new Phrase("Check-in: " + res.getCheckIn(), bodyFont));
            rightCell.addElement(new Phrase("Check-out: " + res.getCheckOut(), bodyFont));
            infoTable.addCell(rightCell);

            document.add(infoTable);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Billing Table
            PdfPTable billTable = new PdfPTable(2);
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 3f, 1f });

            addHeaderCell(billTable, "Description", headFont, navyColor);
            addHeaderCell(billTable, "Price", headFont, navyColor);

            addBillCell(billTable, "Accommodation - " + res.getRoomType() + " (Room " + res.getRoomNumber() + ")",
                    bodyFont);
            addBillCell(billTable, "$" + String.format("%.2f", res.getPricePerNight()), bodyFont);

            if (payment != null) {
                addBillCell(billTable, "Subtotal", bodyFont);
                addBillCell(billTable, "$" + String.format("%.2f", payment.getAmount()), bodyFont);

                addBillCell(billTable, "Tax (10%)", bodyFont);
                addBillCell(billTable, "$" + String.format("%.2f", payment.getTax()), bodyFont);

                addBillCell(billTable, "Service Charge (5%)", bodyFont);
                addBillCell(billTable, "$" + String.format("%.2f", payment.getServiceCharge()), bodyFont);

                PdfPCell totalLabel = new PdfPCell(new Phrase("GRAND TOTAL", headFont));
                totalLabel.setBackgroundColor(goldColor);
                totalLabel.setPadding(10);
                billTable.addCell(totalLabel);

                PdfPCell totalVal = new PdfPCell(new Phrase("$" + String.format("%.2f", payment.getTotal()), headFont));
                totalVal.setBackgroundColor(goldColor);
                totalVal.setPadding(10);
                totalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                billTable.addCell(totalVal);
            }

            document.add(billTable);

            // Footer
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            Paragraph terms = new Paragraph("Terms & Conditions:", boldFont);
            document.add(terms);
            document.add(new Paragraph(
                    "1. This is a computer-generated invoice.\n2. All payments are non-refundable unless specified otherwise.\n3. Please keep this for your records.",
                    bodyFont));

            document.add(new Paragraph(" "));
            Paragraph thanks = new Paragraph("Thank you for choosing Ocean View Resort!",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, goldColor));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(color);
        cell.setPadding(10);
        cell.setBorderColor(BaseColor.WHITE);
        table.addCell(cell);
    }

    private void addBillCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(10);
        cell.setBorderColor(new BaseColor(230, 230, 230));
        table.addCell(cell);
    }
}
