package com.sjf.open.exception;

/**
 * 解析命令行异常
 *
 * Created by xiaosi on 17-6-29.
 */
public class ParseException extends Exception {

    /**
     * 指定异常信息
     * @param message
     */
    public ParseException(String message) {
        super(message);
    }

}
