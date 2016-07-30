package com.sjf.open.flight;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.io.compress.Lz4Codec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobPriority;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 * Created by xiaosi on 16-7-27.
 */
public class FlightConfigure extends Configured implements Tool{
    public int run(String[] args) throws Exception {
        if(args.length != 2) {
            System.err.println("./xxxx <input> <output>");
            System.exit(1);
        }
        String inputPath =  args[0];
        String outputPath = args[1];

        Configuration conf = this.getConf();
        conf.set("mapred.job.queue.name", "wirelessdev");
        conf.set("mapreduce.map.memory.mb", "1024");
        conf.set("mapreduce.reduce.memory.mb", "8192");
        conf.set("mapred.job.priority", JobPriority.VERY_HIGH.name());

        conf.setBoolean("mapred.compress.map.output", true);
        conf.setClass("mapred.map.output.compression.codec", Lz4Codec.class, CompressionCodec.class);
        conf.setBoolean("mapred.output.compress", true);
        conf.setClass("mapred.output.compression.codec", GzipCodec.class, CompressionCodec.class);

        Job job  = Job.getInstance(conf);
        job.setJobName("flight_job_by_xiaosi");
        job.setJarByClass(FlightConfigure.class);

        job.setMapperClass(FlightMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(FlightReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job,inputPath);
        FileOutputFormat.setOutputPath(job,new Path(outputPath));
        boolean success = job.waitForCompletion(true);
        return success?0:1;
    }
}
