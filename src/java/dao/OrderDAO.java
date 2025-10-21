/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Order;
import model.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author trong
 */
public class OrderDAO {

    public int createOrder(Order order) throws SQLException {
        String orderQuery = "INSERT INTO orders (customerName, customerPhone, customerAddress, orderDate, totalAmount, status) VALUES (?, ?, ?, ?, ?, ?)";
        String itemQuery = "INSERT INTO order_items (orderId, productId, productName, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        int orderId = -1;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert order
            try (PreparedStatement pstmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, order.getCustomerName());
                pstmt.setString(2, order.getCustomerPhone());
                pstmt.setString(3, order.getCustomerAddress());
                pstmt.setTimestamp(4, order.getOrderDate());
                pstmt.setDouble(5, order.getTotalAmount());
                pstmt.setString(6, order.getStatus());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }

            // Insert order items
            try (PreparedStatement pstmt = conn.prepareStatement(itemQuery)) {
                for (OrderItem item : order.getItems()) {
                    pstmt.setInt(1, orderId);
                    pstmt.setInt(2, item.getProductId());
                    pstmt.setString(3, item.getProductName());
                    pstmt.setInt(4, item.getQuantity());
                    pstmt.setDouble(5, item.getPrice());
                    pstmt.setDouble(6, item.getSubtotal());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }

        return orderId;
    }

    public Order getOrderById(int orderId) throws SQLException {
        String orderQuery = "SELECT * FROM orders WHERE id = ?";
        String itemsQuery = "SELECT * FROM order_items WHERE orderId = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            Order order = null;

            // Get order details
            try (PreparedStatement pstmt = conn.prepareStatement(orderQuery)) {
                pstmt.setInt(1, orderId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    order = new Order(
                            rs.getInt("id"),
                            rs.getString("customerName"),
                            rs.getString("customerPhone"),
                            rs.getString("customerAddress"),
                            rs.getTimestamp("orderDate"),
                            rs.getDouble("totalAmount"),
                            rs.getString("status")
                    );
                }
            }

            if (order != null) {
                // Get order items
                try (PreparedStatement pstmt = conn.prepareStatement(itemsQuery)) {
                    pstmt.setInt(1, orderId);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        OrderItem item = new OrderItem(
                                rs.getInt("id"),
                                rs.getInt("orderId"),
                                rs.getInt("productId"),
                                rs.getString("productName"),
                                rs.getInt("quantity"),
                                rs.getDouble("price"),
                                rs.getDouble("subtotal")
                        );
                        order.addItem(item);
                    }
                }
            }

            return order;
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY orderDate DESC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getString("customerName"),
                        rs.getString("customerPhone"),
                        rs.getString("customerAddress"),
                        rs.getTimestamp("orderDate"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status")
                );
                orders.add(order);
            }
        }
        return orders;
    }

    public static Map<String, Integer> getPeakOrderHours() throws SQLException {
        Map<String, Integer> hourlyOrders = new HashMap<>();
        String query = "SELECT HOUR(orderDate) as hour, COUNT(*) as count FROM orders GROUP BY HOUR(orderDate) ORDER BY count DESC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String hour = rs.getInt("hour") + ":00";
                int count = rs.getInt("count");
                hourlyOrders.put(hour, count);
            }
        }
        return hourlyOrders;
    }

    public static Map<String, Integer> getBestSellingProducts() throws SQLException {
        Map<String, Integer> productSales = new HashMap<>();
        String query = "SELECT productName, SUM(quantity) as totalQuantity FROM order_items GROUP BY productName ORDER BY totalQuantity DESC";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String productName = rs.getString("productName");
                int totalQuantity = rs.getInt("totalQuantity");
                productSales.put(productName, totalQuantity);
            }
        }
        return productSales;
    }

    public double getTotalRevenue() throws SQLException {
        String query = "SELECT SUM(totalAmount) as revenue FROM orders";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getDouble("revenue");
            }
        }
        return 0.0;
    }
}
