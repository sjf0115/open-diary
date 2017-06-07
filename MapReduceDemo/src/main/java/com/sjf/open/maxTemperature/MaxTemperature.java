package com.sjf.open.maxTemperature;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created by xiaosi on 16-7-27.
 */
public class MaxTemperature extends Configured implements Tool{

    public static void main(String[] args) throws Exception {
        int status = ToolRunner.run(new MaxTemperature(), args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        if(args.length != 2){
            System.err.println("Usage: MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        String inputPath = args[0];
        String outputPath = args[1];

        Job job = Job.getInstance();

        job.setJarByClass(MaxTemperature.class);
        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        boolean success = job.waitForCompletion(true);
        return success ? 0 : 1;
    }
}
