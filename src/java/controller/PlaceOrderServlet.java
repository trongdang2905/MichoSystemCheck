/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.ProductDAO;
import model.Order;
import model.OrderItem;
import model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author trong
 */
@WebServlet(name = "PlaceOrderServlet", urlPatterns = {"/placeOrder"})
public class PlaceOrderServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PlaceOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PlaceOrderServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> products = productDAO.getAllProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("order.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get customer information
            String customerName = request.getParameter("customerName");
            String customerPhone = request.getParameter("customerPhone");
            String customerAddress = request.getParameter("customerAddress");

            // Get product IDs and quantities
            String[] productIds = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");

            if (productIds == null || quantities == null) {
                response.sendRedirect("placeOrder?error=no_items");
                return;
            }

            // Create order
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setCustomerPhone(customerPhone);
            order.setCustomerAddress(customerAddress);
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");

            double totalAmount = 0.0;

            // Add order items
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                if (quantity > 0) {
                    Product product = productDAO.getProductById(productId);
                    if (product != null) {
                        double subtotal = product.getPrice() * quantity;

                        OrderItem item = new OrderItem();
                        item.setProductId(productId);
                        item.setProductName(product.getName());
                        item.setQuantity(quantity);
                        item.setPrice(product.getPrice());
                        item.setSubtotal(subtotal);

                        order.addItem(item);
                        totalAmount += subtotal;

                        // Update stock
                        productDAO.updateStock(productId, quantity);
                    }
                }
            }

            order.setTotalAmount(totalAmount);

            // Save order to database
            int orderId = orderDAO.createOrder(order);

            // Redirect to invoice
            response.sendRedirect("exportInvoice?orderId=" + orderId);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (NumberFormatException e) {
            response.sendRedirect("placeOrder?error=invalid_input");
        }
    
}

/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
@Override
public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
