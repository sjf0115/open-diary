package com.sjf.open.json.model;

import com.sjf.open.json.annotation.Excluded;

/**
 * Created by xiaosi on 16-11-17.
 */
public class School {

    private String name;
    private int count;
    @Excluded
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "B{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", location='" + location + '\'' +
                '}';
    }

}
