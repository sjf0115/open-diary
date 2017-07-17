package com.sjf.open.ltp;

import com.sjf.open.utils.HttpUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by xiaosi on 17-7-15.
 */
public class LTPUrlTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String apiKey = "a193H0o4c38ysUBcZqGh2MTvBNjHxQFJDRpIQN7q";
        String path = "http://api.ltp-cloud.com/analysis/";
        String params = "api_key="+apiKey+"&text='面料非常好'&pattern=sdb_graph&format=json";
        String json = HttpUtil.sendPost(path, params);
        System.out.println(json);
    }
}
