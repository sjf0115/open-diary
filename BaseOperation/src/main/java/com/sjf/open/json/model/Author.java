package com.sjf.open.json.model;

/**
 * Created by xiaosi on 16-7-13.
 */
public class Author {

    private String name;
    private int age;
    private transient String sex;
    private String favorite;

    public Author(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Author() {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", favorite='" + favorite + '\'' +
                '}';
    }
}
