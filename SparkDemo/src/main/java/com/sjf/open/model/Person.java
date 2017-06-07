package com.sjf.open.model;

import java.io.Serializable;

/**
 * Created by xiaosi on 17-6-7.
 */
public class Person implements Serializable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
