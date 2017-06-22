package com.sjf.open.model;

import java.io.Serializable;

/**
 * Created by xiaosi on 17-6-13.
 */
public class Square implements Serializable{
    private int value;
    private int square;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSquare() {
        return square;
    }

    public void setSquare(int square) {
        this.square = square;
    }
}
