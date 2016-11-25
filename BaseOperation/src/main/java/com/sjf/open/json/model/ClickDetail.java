package com.sjf.open.json.model;

/**
 * Created by xiaosi on 16-7-13.
 */
public class ClickDetail{
    private String business;
    private double price;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ClickDetail{" +
                "business='" + business + '\'' +
                ", price=" + price +
                '}';
    }
}
