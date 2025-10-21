/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.Order;
import model.OrderItem;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 *
 * @author trong
 */
public class InvoiceGenerator {

    public static void generatePDF(Order order, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + order.getId() + ".pdf");

        // For actual PDF generation, use libraries like iText or Apache PDFBox
        // This is a simplified HTML version
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Invoice</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 40px; }");
        out.println(".invoice { border: 1px solid #ddd; padding: 20px; }");
        out.println(".header { text-align: center; margin-bottom: 30px; }");
        out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println(".total { font-size: 18px; font-weight: bold; text-align: right; margin-top: 20px; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<div class='invoice'>");
        out.println("<div class='header'>");
        out.println("<h1>MICHO ORDERING SYSTEM</h1>");
        out.println("<h2>INVOICE</h2>");
        out.println("</div>");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        out.println("<p><strong>Invoice #:</strong> " + order.getId() + "</p>");
        out.println("<p><strong>Date:</strong> " + sdf.format(order.getOrderDate()) + "</p>");
        out.println("<p><strong>Customer:</strong> " + order.getCustomerName() + "</p>");
        out.println("<p><strong>Phone:</strong> " + order.getCustomerPhone() + "</p>");
        out.println("<p><strong>Address:</strong> " + order.getCustomerAddress() + "</p>");

        out.println("<table>");
        out.println("<tr><th>Product</th><th>Quantity</th><th>Price</th><th>Subtotal</th></tr>");

        for (OrderItem item : order.getItems()) {
            out.println("<tr>");
            out.println("<td>" + item.getProductName() + "</td>");
            out.println("<td>" + item.getQuantity() + "</td>");
            out.println("<td>$" + String.format("%.2f", item.getPrice()) + "</td>");
            out.println("<td>$" + String.format("%.2f", item.getSubtotal()) + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<div class='total'>Total: $" + String.format("%.2f", order.getTotalAmount()) + "</div>");
        out.println("</div>");

        out.println("</body></html>");
    }
}
