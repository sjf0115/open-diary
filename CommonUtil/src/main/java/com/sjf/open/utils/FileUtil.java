package com.sjf.open.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Created by xiaosi on 16-7-12.
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 写文件
     * 
     * @param fileName
     * @param contents
     */
    public static void WriteToFile(String fileName, String contents) {
        checkNotNull(fileName, "Provided file name for writing must NOT be null.");
        checkNotNull(contents, "Unable to write null contents.");

        try {
            File newFile = new File(fileName);
            Files.write(contents, newFile, Charsets.UTF_8);
        } catch (IOException e) {
            logger.error("----------------- FileUtil --- WriteToFile", e);
        }
    }

    /**
     * 追加文件
     * 
     * @param fileName
     * @param contents
     */
    public static void appendToFile(String fileName, String contents) {
        checkNotNull(fileName, "Provided file name for writing must NOT be null.");
        checkNotNull(contents, "Unable to write null contents.");

        try {
            File newFile = new File(fileName);
            Files.append(contents, newFile, Charsets.UTF_8);
        } catch (IOException e) {
            logger.info("----------------- FileUtil --- appendToFile", e);
        }
    }

    /**
     * 读取文件
     * 
     * @param path
     * @return
     */
    public static List<String> readLine(String path) {
        List<String> lines = Lists.newArrayList();
        if (StringUtils.isBlank(path)) {
            return lines;
        }
        File testFile = new File(path);
        try {
            lines = Files.readLines(testFile, Charsets.UTF_8);
        } catch (IOException e) {
            logger.error("--------- FileUtil --- readLine", e);
        }
        return lines;
    }

    /**
     * 从Resource下读取文件
     * 
     * @param fileName
     * @return
     */
    public static List<String> readLineFromResource(String fileName) {
        List<String> lines = Lists.newArrayList();
        if (StringUtils.isBlank(fileName)) {
            return lines;
        }
        String path = FileUtil.class.getResource("/" + fileName).getPath().toString();
        lines = readLine(path);
        return lines;
    }
}
