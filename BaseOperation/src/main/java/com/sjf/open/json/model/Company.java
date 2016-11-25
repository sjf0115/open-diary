package com.sjf.open.json.model;

import com.google.gson.annotations.Expose;

/**
 * Created by xiaosi on 16-11-17.
 */
public class Company {

    @Expose
    private String name;
    private String location;
    @Expose
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", count=" + count +
                '}';
    }
}
