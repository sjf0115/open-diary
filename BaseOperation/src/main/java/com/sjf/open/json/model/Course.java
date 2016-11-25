package com.sjf.open.json.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaosi on 16-11-17.
 */
public class Course {
    @SerializedName("course_name")
    private String name;
    @SerializedName("courser_teacher_name")
    private String teacher;
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", count=" + count +
                '}';
    }
}
