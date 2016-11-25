package com.sjf.open.json.model;

/**
 * Created by xiaosi on 16-11-16.
 */
public class Fruit {

    private String name;
    private transient double price;
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", location='" + location + '\'' +
                '}';
    }
}
