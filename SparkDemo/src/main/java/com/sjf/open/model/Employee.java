package com.sjf.open.model;

import java.io.Serializable;

/**
 * Created by xiaosi on 17-6-7.
 */
public class Employee implements Serializable{

    private String name;
    private long salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
