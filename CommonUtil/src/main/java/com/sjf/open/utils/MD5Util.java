package com.sjf.open.utils;

import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-12-5.
 *
 * MD5加密
 *
 */
public class MD5Util {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    /**
     *  md5 加密
     * @param input 待加密字节
     * @return
     */
    public static String md5(byte[] input) {

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            byte[] digestArray = messageDigest.digest(input);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digestArray.length; i++) {
                stringBuilder.append(String.format("%02x", digestArray[i] & 0xff));
            }
            return stringBuilder.toString();

        } catch (Exception e) {
            logger.error("MD5Util --- md5 --- Exception:",e);
            return null;
        }

    }

    /**
     * md5 加密
     * @param s 待加密字符串
     * @return
     */
    public static String md5(String s) {

        if(StringUtils.isBlank(s)){
            return null;
        }

        return md5(s, "UTF-8");

    }

    /**
     * md5 加密
     * @param s 待加密字符串
     * @param charsetName 编码格式
     * @return
     */
    public static String md5(String s, String charsetName) {

        if(StringUtils.isBlank(s) || StringUtils.isBlank(charsetName)){
            return null;
        }

        try {
            return md5(s.getBytes(charsetName));
        } catch (Exception e) {
            logger.error("MD5Util --- md5 --- Exception:",e);
            return null;
        }

    }

}
