package com.sjf.open.base;

/**
 * Created by xiaosi on 16-12-7.
 */
public class Address implements Cloneable{
    private String province;
    private String city;

    public String street;

    public Address() {
    }

    public Address(String province) {
        this.province = province;
    }

    public Address(String province, String city) {
        this.province = province;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    private String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    protected Address clone() throws CloneNotSupportedException {
        return (Address) super.clone();
    }
}
