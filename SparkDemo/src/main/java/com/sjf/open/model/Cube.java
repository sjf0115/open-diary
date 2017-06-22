package com.sjf.open.model;

import java.io.Serializable;

/**
 * Created by xiaosi on 17-6-13.
 */
public class Cube implements Serializable{
    private int value;
    private int cube;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCube() {
        return cube;
    }

    public void setCube(int cube) {
        this.cube = cube;
    }
}
