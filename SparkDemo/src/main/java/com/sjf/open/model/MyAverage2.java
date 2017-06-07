package com.sjf.open.model;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.expressions.Aggregator;

/**
 * Created by xiaosi on 17-6-7.
 */
public class MyAverage2 extends Aggregator<Employee, Average, Double>{
    @Override
    public Average zero() {
        return new Average(0L, 0L);
    }

    @Override
    public Average reduce(Average b, Employee a) {
        return null;
    }

    @Override
    public Average merge(Average b1, Average b2) {
        return null;
    }

    @Override
    public Double finish(Average reduction) {
        return null;
    }

    @Override
    public Encoder<Average> bufferEncoder() {
        return null;
    }

    @Override
    public Encoder<Double> outputEncoder() {
        return null;
    }
}
