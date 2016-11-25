package com.sjf.open.json.model;

/**
 * Created by xiaosi on 16-11-16.
 */
public class Result<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Result{" +
                "value=" + value +
                '}';
    }
}
