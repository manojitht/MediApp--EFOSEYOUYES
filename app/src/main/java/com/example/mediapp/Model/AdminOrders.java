package com.example.mediapp.Model;

public class AdminOrders {
    private String Cname, address, city, date, phone, status, time, totalAmount;
    public AdminOrders(){
    }

    public AdminOrders(String cname, String address, String city, String date, String phone, String status, String time, String totalAmount) {
        Cname = cname;
        this.address = address;
        this.city = city;
        this.date = date;
        this.phone = phone;
        this.status = status;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}