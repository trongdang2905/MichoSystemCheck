/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import dao.OrderDAO;
import model.SaleReport;

import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author trong
 */
public class ReportGenerator {

    public static SaleReport generateSalesReport(OrderDAO orderDAO) throws SQLException {
        SaleReport report = new SaleReport();

        // Get total revenue
        double totalRevenue = orderDAO.getTotalRevenue();
        report.setTotalRevenue(totalRevenue);

        // Get total orders
        int totalOrders = orderDAO.getAllOrders().size();
        report.setTotalOrders(totalOrders);

        // Get best selling products
        Map<String, Integer> bestSellers = orderDAO.getBestSellingProducts();
        if (!bestSellers.isEmpty()) {
            Map.Entry<String, Integer> topProduct = bestSellers.entrySet().iterator().next();
            report.setBestSellingProduct(topProduct.getKey());
            report.setBestSellingQuantity(topProduct.getValue());
        }

        // Get peak hours
        Map<String, Integer> peakHours = orderDAO.getPeakOrderHours();
        if (!peakHours.isEmpty()) {
            Map.Entry<String, Integer> peakHour = peakHours.entrySet().iterator().next();
            report.setPeakHour(peakHour.getKey());
            report.setPeakHourOrders(peakHour.getValue());
        }

        report.setPeriod("All Time");

        return report;
    }
}
