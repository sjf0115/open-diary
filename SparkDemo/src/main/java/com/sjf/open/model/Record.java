package com.sjf.open.model;

import java.io.Serializable;

/**
 * Created by xiaosi on 17-6-9.
 */
public class Record implements Serializable{
    private String value;
    private int key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
