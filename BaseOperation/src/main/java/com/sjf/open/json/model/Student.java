package com.sjf.open.json.model;

import com.google.gson.annotations.Since;

/**
 * Created by xiaosi on 16-11-16.
 */
public class Student {
    @Since(1.1)
    private String nickName;
    @Since(1.0)
    private String name;
    private int age;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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

    @Override
    public String toString() {
        return "Student{" +
                "nickName='" + nickName + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
