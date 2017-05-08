package com.sjf.open.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;


/**
 * Created by xiaosi on 17-1-11.
 *
 * 读取配置文件
 *
 */
public class PropertyUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    private static Map<String, String> propsMap = Maps.newHashMap();

    /**
     * 初始化
     * @param path
     */
    public static void init(String path){
        if(StringUtils.isBlank(path)){
            return;
        }
        props = new Properties();
        try {
            props.load(PropertyUtil.class.getClassLoader().getResourceAsStream(path));
            Set keys = props.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext();){
                String key = iterator.next().toString();
                propsMap.put(key, props.getProperty(key));
            }
        } catch (Exception e) {
            logger.error("---------- PropertyUtil --- Init", e);
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

    public static void main(String[] args) {
        PropertyUtil.init("data/cluster.properties");
        Map<String, String> map = PropertyUtil.getAllFilterProps();
        for(String key : map.keySet()){
            System.out.println(key + " --- " + map.get(key));
        }
    }

}
