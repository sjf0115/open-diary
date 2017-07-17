package com.sjf.open.utils;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xiaosi on 17-7-15.
 */
public class FileUtil {

    public static List<String> read(String path){

        List<String> list = Lists.newArrayList();

        if(StringUtils.isBlank(path)){
            return list;
        }

        File file = new File(path);
        if(!file.exists()){
            return list;
        }
        try {
            list = Files.readLines(file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;

    }

}
