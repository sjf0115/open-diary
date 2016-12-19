package com.sjf.open.base;


/**
 * Created by xiaosi on 16-12-7.
 */
public class Teacher implements Cloneable{

    private String name = "";
    private int age;
    private Address address;


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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }

    @Override
    protected Teacher clone() throws CloneNotSupportedException {
        Teacher teacher = (Teacher)super.clone();
        teacher.address = address.clone();
        return teacher;
    }
}
