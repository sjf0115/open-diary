package com.sjf.open.model;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.expressions.Aggregator;

/**
 * Created by xiaosi on 17-6-7.
 */
public class MyAverage2 extends Aggregator<Employee, Average, Double>{

    // 聚合的零值 应满足b + zero = b的属性
    @Override
    public Average zero() {
        return new Average(0L, 0L);
    }

    // 组合两个值以产生一个新值 为了性能,函数可以修改buffer并返回,而不是构造一个新的对象
    @Override
    public Average reduce(Average buffer, Employee employee) {
        long newSum = buffer.getSum() + employee.getSalary();
        long newCount = buffer.getCount() + 1;
        buffer.setSum(newSum);
        buffer.setCount(newCount);
        return buffer;
    }

    // 合并两个中间值
    @Override
    public Average merge(Average b1, Average b2) {
        long mergedSum = b1.getSum() + b2.getSum();
        long mergedCount = b1.getCount() + b2.getCount();
        b1.setSum(mergedSum);
        b1.setCount(mergedCount);
        return b1;
    }

    // 转换聚合的输出
    @Override
    public Double finish(Average reduction) {
        return ((double) reduction.getSum()) / reduction.getCount();
    }

    // 指定中间值类型的编码器
    @Override
    public Encoder<Average> bufferEncoder() {
        return Encoders.bean(Average.class);
    }

    // 指定最终输出值类型的编码器
    @Override
    public Encoder<Double> outputEncoder() {
        return Encoders.DOUBLE();
    }
}
