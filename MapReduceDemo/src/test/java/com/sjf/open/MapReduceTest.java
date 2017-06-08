package com.sjf.open;

import com.sjf.open.maxTemperature.MaxTemperature;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by xiaosi on 17-6-8.
 */
public class MapReduceTest {
    private Configuration conf = new Configuration();
    private Path inputPath;
    private Path outputPath;

    private String baseInputStr = "/home/xiaosi/test/input";
    private String outputStr = "/home/xiaosi/test/output";


    @Before
    public void setUp() throws IOException {
        conf.set("fs.default.name", "file:///");
        conf.set("mapred.job.tracker", "local");

        outputPath = new Path(outputStr);

        FileSystem fileSystem = FileSystem.getLocal(conf);
        fileSystem.delete(outputPath, true);
    }

    @Test
    public void MapReduceTest() throws Exception {

        String path = baseInputStr + "/maxTemperature";
        inputPath = new Path(path);
        outputPath = new Path(outputStr);

        MaxTemperature maxTemperature = new MaxTemperature();
        maxTemperature.setConf(conf);

        int exitCode = maxTemperature.run(new String[] {inputPath.toString(), outputPath.toString()});
        assertThat(exitCode, is(0));
    }
}
