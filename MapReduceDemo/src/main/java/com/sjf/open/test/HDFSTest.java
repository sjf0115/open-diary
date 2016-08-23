package com.sjf.open.test;

import com.google.common.base.Objects;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by xiaosi on 16-7-26.
 */
public class HDFSTest {

    private static Logger logger = LoggerFactory.getLogger(HDFSTest.class);

    private static final String rootPath = "hdfs://127.0.0.1:9000/";
    private static final String textPath = "/user/xiaosi/data/mysql-result.txt";

    private static FileSystem fileSystem = null;

    static {
        Configuration config = new Configuration();
        URI uri = URI.create(rootPath);
        try {
            fileSystem = FileSystem.get(uri, config);
        } catch (IOException e) {
            logger.error("--------- 获取FileSystem失败 {}",e);
        }
    }

    /**
     * read
     * @throws IOException
     */
    private static void readFromHDFS() throws IOException {

        if(Objects.equal(fileSystem, null)){
            return;
        }//if

        FSDataInputStream fsDataInputStream = null;
        try {
            // 调用open函数获取文件的输入流
            fsDataInputStream = fileSystem.open(new Path("/user/hadoop/weather/1902"));
            IOUtils.copyBytes(fsDataInputStream, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(fsDataInputStream);
        }
    }

    /**
     * 定位文件位置
     * @throws IOException
     */
    private static void seek() throws IOException {
        if(Objects.equal(fileSystem, null)){
            return;
        }//if

        FSDataInputStream fsDataInputStream = null;
        try {
            // 调用open函数获取文件的输入流
            fsDataInputStream = fileSystem.open(new Path(textPath));
            // 写入到标准输出中
            IOUtils.copyBytes(fsDataInputStream, System.out, 4096, false);
            // 定位到文件起始位置
            fsDataInputStream.seek(0);

            logger.info("--------- 第二次读取 ---------");

            // 调用open函数获取文件的输入流
            fsDataInputStream = fileSystem.open(new Path(textPath));
            // 写入到标准输出中
            IOUtils.copyBytes(fsDataInputStream, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(fsDataInputStream);
        }
    }

    /**
     *  将本地文件拷贝到Hadoop文件系统
     * @throws IOException
     */
    private static void copy() throws IOException {
        String sourcePath = "/home/xiaosi/data/test/num.txt";
        String targetPath = "/user/xiaosi/data/num.txt";

        InputStream inputStream = new BufferedInputStream(new FileInputStream(sourcePath));

        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(targetPath), new Progressable() {
            public void progress() {
                System.out.println(".");
            }
        });

        IOUtils.copyBytes(inputStream, fsDataOutputStream, 4096, true);
    }



    public static void main(String[] args) throws IOException {
        copy();
    }
}
