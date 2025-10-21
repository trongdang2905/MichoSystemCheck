/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author trong
 */
public class SaleReport {

    private String period;
    private int totalOrders;
    private double totalRevenue;
    private String bestSellingProduct;
    private int bestSellingQuantity;
    private String peakHour;
    private int peakHourOrders;

    public SaleReport() {
    }

    // Getters and Setters
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getBestSellingProduct() {
        return bestSellingProduct;
    }

    public void setBestSellingProduct(String bestSellingProduct) {
        this.bestSellingProduct = bestSellingProduct;
    }

    public int getBestSellingQuantity() {
        return bestSellingQuantity;
    }

    public void setBestSellingQuantity(int bestSellingQuantity) {
        this.bestSellingQuantity = bestSellingQuantity;
    }

    public String getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(String peakHour) {
        this.peakHour = peakHour;
    }

    public int getPeakHourOrders() {
        return peakHourOrders;
    }

    public void setPeakHourOrders(int peakHourOrders) {
        this.peakHourOrders = peakHourOrders;
    }
}
