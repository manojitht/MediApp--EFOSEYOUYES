package com.example.mediapp.Model;

public class CustomerOrder {
    private String orderId, status, date, time, address, totalAmount;
    public CustomerOrder(){
    }

    public CustomerOrder(String address, String date, String status, String time, String totalAmount, String orderId) {
        this.address = address;
        this.date = date;
        this.status = status;
        this.time = time;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
