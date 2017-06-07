package com.sjf.open.model;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaosi on 17-6-7.
 */
public class MyAverage extends UserDefinedAggregateFunction{

    private StructType inputSchema;
    private StructType bufferSchema;

    public MyAverage() {
        List<StructField> inputFields = new ArrayList<>();
        inputFields.add(DataTypes.createStructField("inputColumn", DataTypes.LongType, true));
        inputSchema = DataTypes.createStructType(inputFields);

        List<StructField> bufferFields = new ArrayList<>();
        bufferFields.add(DataTypes.createStructField("sum", DataTypes.LongType, true));
        bufferFields.add(DataTypes.createStructField("count", DataTypes.LongType, true));
        bufferSchema = DataTypes.createStructType(bufferFields);
    }

    // 聚合函数输入参数数据类型
    @Override
    public StructType inputSchema() {
        return inputSchema;
    }

    // 聚合函数缓冲数据的数据类型
    @Override
    public StructType bufferSchema() {
        return bufferSchema;
    }

    // 返回值的数据类型
    @Override
    public DataType dataType() {
        return DataTypes.DoubleType;
    }

    // 这个函数是否总是在相同的输入上返回相同的输出
    @Override
    public boolean deterministic() {
        return true;
    }

    // 初始化聚合缓冲区 缓冲区本身是一个Row
    // 除了提供标准方法　如在索引中检索值(例如 get() getBoolean())外
    // 还提供了更新其值的机会　缓冲区内的数组和映射仍然是不可变的
    @Override
    public void initialize(MutableAggregationBuffer buffer) {
        buffer.update(0, 0L);
        buffer.update(1, 0L);
    }

    // 使用input的新输入数据更新给定的聚合缓冲区buffer
    @Override
    public void update(MutableAggregationBuffer buffer, Row input) {
        if (!input.isNullAt(0)) {
            long updatedSum = buffer.getLong(0) + input.getLong(0);
            long updatedCount = buffer.getLong(1) + 1;
            buffer.update(0, updatedSum);
            buffer.update(1, updatedCount);
        }
    }

    // 合并两个聚合缓冲区并将更新的缓冲区值存储回buffer1
    @Override
    public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
        long mergedSum = buffer1.getLong(0) + buffer2.getLong(0);
        long mergedCount = buffer1.getLong(1) + buffer2.getLong(1);
        buffer1.update(0, mergedSum);
        buffer1.update(1, mergedCount);
    }

    // 计算最终结果
    @Override
    public Object evaluate(Row buffer) {
        return ((double) buffer.getLong(0)) / buffer.getLong(1);
    }
}
