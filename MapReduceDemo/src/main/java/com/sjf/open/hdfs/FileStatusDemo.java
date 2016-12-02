package com.sjf.open.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * Created by xiaosi on 16-12-1.
 */
public class FileStatusDemo {

    public static void test() throws IOException {

        String pathStr = "/user/xiaosi/order_info";

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        //FileStatus对象封装了文件的和目录的额元数据，包括文件长度、块大小、权限等信息
        Path path = new Path(pathStr);
        FileStatus fileStatus = fs.getFileStatus(path);

        System.out.println("文件路径："+fileStatus.getPath());
        System.out.println("块的大小："+fileStatus.getBlockSize());
        System.out.println("文件所有者："+fileStatus.getOwner()+":"+fileStatus.getGroup());
        System.out.println("文件权限："+fileStatus.getPermission());
        System.out.println("文件长度："+fileStatus.getLen());
        System.out.println("备份数："+fileStatus.getReplication());
        System.out.println("修改时间："+fileStatus.getModificationTime());
    }

    public static void main(String[] args) throws IOException {
        test();
    }

}
