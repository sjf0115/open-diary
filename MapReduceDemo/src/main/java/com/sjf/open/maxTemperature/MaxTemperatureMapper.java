package com.sjf.open.maxTemperature;

import com.google.common.base.Objects;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by xiaosi on 16-7-27.
 */
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final int MISSING = 9999;

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString();
        // 年份
        String year = line.substring(15, 19);
        // 温度
        int airTemperature;
        if(Objects.equal(line.charAt(87),"+")){
            airTemperature = Integer.parseInt(line.substring(88,92));
        }
        else{
            airTemperature = Integer.parseInt(line.substring(87,92));
        }
        // 空气质量
        String quality = line.substring(92, 93);
        if(!Objects.equal(airTemperature, MISSING) && quality.matches("[01459]")){
            context.write(new Text(year), new IntWritable(airTemperature));
        }
    }
}
