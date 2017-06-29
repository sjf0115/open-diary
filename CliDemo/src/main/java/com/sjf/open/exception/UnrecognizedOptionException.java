package com.sjf.open.exception;

/**
 * 无法识别选项异常
 *
 * Created by xiaosi on 17-6-29.
 */
public class UnrecognizedOptionException extends ParseException {

    // 无法识别的选项
    private String option;

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public UnrecognizedOptionException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param option 无法识别的选项
     */
    public UnrecognizedOptionException(String message, String option) {
        this(message);
        this.option = option;
    }

    /**
     * 返回无法识别的选项
     * @return
     */
    public String getOption()
    {
        return option;
    }
}
