package com.sjf.open.maxTemperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by xiaosi on 16-7-27.
 */
public class MaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    public void reduce(Text key, Iterator<IntWritable> values, Context context) throws IOException, InterruptedException {
        // 一年最高气温
        int maxValue = Integer.MIN_VALUE;
        while(values.hasNext()){
            IntWritable value = values.next();
            maxValue = Math.max(maxValue, value.get());
        }//while
        // 输出
        context.write(key, new IntWritable(maxValue));
    }
}
