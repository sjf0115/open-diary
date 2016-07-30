package com.sjf.open.maxTemperature;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by xiaosi on 16-7-27.
 */
public class MaxTemperature {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if(args.length != 2){
            System.err.println("Usage: MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        String inputPath = args[0];
        String outputPath = args[1];

        Job job = Job.getInstance();
        job.setJarByClass(MaxTemperature.class);

        // 指定输入数据路径
        FileInputFormat.addInputPath(job, new Path(inputPath));
        // 指定输出数据路径
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 指定map类型
        job.setMapperClass(MaxTemperatureMapper.class);
        // 指定reduce类型
        job.setReducerClass(MaxTemperatureReducer.class);

        // 指定map 和 reduce 函数 输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 运行job
        System.exit(job.waitForCompletion(true) ? 0: 1);
    }
}
