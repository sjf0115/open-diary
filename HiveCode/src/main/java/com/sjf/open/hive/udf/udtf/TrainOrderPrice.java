package com.sjf.open.hive.udf.udtf;

/**
 * Created by xiaosi on 16-11-25.
 */
public class TrainOrderPrice {

    private String ticketPrice = "";
    private String servicePrice = "";
    private String insurancePrice = "";

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    @Override
    public String toString() {
        return "TrainOrderPrice{" +
                "ticketPrice='" + ticketPrice + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", insurancePrice='" + insurancePrice + '\'' +
                '}';
    }
}
