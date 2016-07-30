package com.sjf.open.flight;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by xiaosi on 16-7-28.
 */
public class FlightMapper extends Mapper<LongWritable, Text, Text, Text> {

    private static final String HOTDOG_FILE_PATH = "mysql-log/hive_etl_client";
    private static final String KYLIN_FILE_PATH = "marmot_backup/logclean/mbserver";

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context);
    }
}
