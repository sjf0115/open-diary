package com.sjf.open.exception;

import com.sjf.open.bean.Option;

/**
 * 参数丢失异常
 *
 * Created by xiaosi on 17-6-29.
 */
public class MissingArgumentException extends ParseException {

    // 需要额外参数的选项
    private Option option;

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(Option option) {
        this("Missing argument for option: " + option.getKey());
        this.option = option;
    }

    /**
     * 返回需要参数但是并未提供的选项
     * @return
     */
    public Option getOption() {
        return option;
    }
}
