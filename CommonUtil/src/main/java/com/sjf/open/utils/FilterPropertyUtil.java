package com.sjf.open.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;


/**
 * Created by xiaosi on 17-1-11.
 *
 * 读取配置文件 filter.properties
 *
 */
public class FilterPropertyUtil {

    private static Properties props;
    private static Map<String, String> propsMap = Maps.newHashMap();

    static {
        props = new Properties();
        try {
            props.load(FilterPropertyUtil.class.getClassLoader().getResourceAsStream("filter.properties"));
            Set keys = props.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext();){
                String key = iterator.next().toString();
                propsMap.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取单个配置项
     * @param key
     * @return
     */
    public static String getFilterProperty(String key){

        if(StringUtils.isBlank(key)){
            return null;
        }
        return propsMap.get(key);

    }

    /**
     * 读取所有配置文件
     * @return
     */
    public static Map<String, String> getAllFilterProps(){

        return propsMap;

    }

}
