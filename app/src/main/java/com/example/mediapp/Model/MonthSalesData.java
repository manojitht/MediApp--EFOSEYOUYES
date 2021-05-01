package com.example.mediapp.Model;

public class MonthSalesData{
    String month;
    String sales;

    public MonthSalesData(String month, String sales) {
        this.month = month;
        this.sales = sales;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }
}
