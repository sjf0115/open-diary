package com.sjf.open.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xiaosi on 16-10-21.
 */
public class FileUtil {

    public static void read(String path){
        path = FileUtil.class.getResource("/"+path).getPath();
        File file = new File(path);
        try {
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
