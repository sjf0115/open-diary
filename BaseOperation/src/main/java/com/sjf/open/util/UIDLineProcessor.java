package com.sjf.open.util;

import avro.shaded.com.google.common.collect.Lists;
import com.google.common.io.LineProcessor;

import java.io.IOException;
import java.util.List;

/**
 * Created by xiaosi on 16-10-21.
 */
public class UIDLineProcessor implements LineProcessor<String> {

    private List<String> uidList = Lists.newArrayList();

    public boolean processLine(String line) throws IOException {
        try{
            String uid = line.toLowerCase();
            uidList.add(uid);
        }
        catch (Exception e){
            System.out.println("---------------- 转换错误");
        }

        return false;
    }

    public String getResult() {
        return null;
    }

    public List<String> getUidList(){
        return uidList;
    }
}
