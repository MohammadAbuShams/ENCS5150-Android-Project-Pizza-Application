package com.example.myapplication;
import java.io.Serializable;
public class Order implements Serializable{
    private int orderId;
    private String orderDetails;
    private String orderDate;
    private int imageId;
    private String customerName;

    // Constructor without customer name
    public Order(int orderId, String orderDetails, String orderDate, int imageId) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
        this.orderDate = orderDate;
        this.imageId = imageId;
    }

    // Constructor with customer name
    public Order(int orderId, String orderDetails, String orderDate, int imageId, String customerName) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
        this.orderDate = orderDate;
        this.imageId = imageId;
        this.customerName = customerName;
    }

    // Getters and setters for the fields
    public int getOrderId() {
        return orderId;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getImageId() {
        return imageId;
    }

    public String getCustomerName() {
        return customerName;
    }


    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
