package com.sjf.open.model;

/**
 * Created by xiaosi on 16-10-13.
 */
public class FootballPlayer {
    private String name = "";
    private int age;
    private String club = "";
    private String country = "";

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

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "FootballPlayer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", club='" + club + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
