package com.sjf.open.exception;

import com.sjf.open.bean.Option;
import com.sjf.open.bean.OptionGroup;

/**
 * Created by xiaosi on 17-6-29.
 */
public class AlreadySelectedException extends RuntimeException {

    private OptionGroup group;
    private Option option;

    public AlreadySelectedException(final String message) {
        super(message);
    }

    public AlreadySelectedException(final OptionGroup group, final Option option) {
        this("The option '" + option.getKey() + "' was specified but an option from this group "
                + "has already been selected: '" + group.getSelected() + "'");
        this.group = group;
        this.option = option;
    }

}
