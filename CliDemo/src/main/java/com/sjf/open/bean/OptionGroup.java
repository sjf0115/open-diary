package com.sjf.open.bean;

import com.sjf.open.exception.AlreadySelectedException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiaosi on 17-6-29.
 */
public class OptionGroup implements Serializable {

    // 该分组下的Option
    private final Map<String, Option> optionMap = new LinkedHashMap<String, Option>();
    // 选择的Option名称
    private String selected;
    // 是否需要分组
    private boolean required;

    /**
     * 添加Option
     * 
     * @param option
     * @return
     */
    public OptionGroup addOption(final Option option) {
        // key - option name
        // value - the option
        optionMap.put(option.getKey(), option);
        return this;
    }

    /**
     * 分组中所有Option的名称集合
     * 
     * @return
     */
    public Collection<String> getNames() {
        return optionMap.keySet();
    }

    /**
     * 分组中所有Option集合
     * 
     * @return
     */
    public Collection<Option> getOptions() {
        return optionMap.values();
    }

    /**
     *
     * @param option
     * @throws AlreadySelectedException
     */
    public void setSelected(final Option option) throws AlreadySelectedException {
        if (option == null) {
            selected = null;
            return;
        }

        if (selected == null || selected.equals(option.getKey())) {
            selected = option.getKey();
        } else {
            throw new AlreadySelectedException(this, option);
        }
    }

    /**
     *
     * @return
     */
    public String getSelected() {
        return selected;
    }

    /**
     *
     * @param required
     */
    public void setRequired(final boolean required) {
        this.required = required;
    }

    /**
     *
     * @return
     */
    public boolean isRequired() {
        return required;
    }

    @Override
    public String toString() {

        final StringBuilder buff = new StringBuilder();
        final Iterator<Option> iter = getOptions().iterator();

        buff.append("[");
        while (iter.hasNext()) {
            final Option option = iter.next();

            if (option.getOpt() != null) {
                buff.append("-");
                buff.append(option.getOpt());
            } else {
                buff.append("--");
                buff.append(option.getLongOpt());
            }

            if (option.getDescription() != null) {
                buff.append(" ");
                buff.append(option.getDescription());
            }

            if (iter.hasNext()) {
                buff.append(", ");
            }
        }

        buff.append("]");

        return buff.toString();
    }
}
