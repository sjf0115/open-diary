package com.sjf.open;

import org.apache.thrift.TException;

/**
 * Created by xiaosi on 16-9-20.
 */
public class CalculatorHandler implements CalculatorService.Iface{
    public int add(int num1, int num2) throws TException {
        return num1 + num2;
    }

    public int minus(int num1, int num2) throws TException {
        return num1 - num2;
    }

    public int multi(int num1, int num2) throws TException {
        return num1 * num2;
    }

    public int divi(int num1, int num2) throws TException {
        if(num2 == 0){
            throw new RuntimeException("分母不能为0");
        }
        return num1 / num2;
    }
}
