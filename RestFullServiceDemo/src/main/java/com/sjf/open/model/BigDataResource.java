package com.sjf.open.model;

/**
 * Created by xiaosi on 16-9-5.
 */
public class BigDataResource {
    private String name;
    private String type;
    private String desc;

    public BigDataResource(String name, String type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BigDataResource{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
