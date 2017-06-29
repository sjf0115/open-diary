package com.sjf.open.bean;

import com.sjf.open.OptionValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令行选项
 *
 * Created by xiaosi on 17-6-29.
 */
public class Option implements Cloneable, Serializable {
    // 未指定参数值数量
    public static final int UNINITIALIZED = -1;
    // 参数值数量无上限
    public static final int UNLIMITED_VALUES = -2;

    private String opt;
    private String longOpt;
    private String argName;
    private String description;
    // 判断选项是否存在
    private boolean required;
    // 判断选项的参数值(argument value)是否可选
    private boolean optionalArg;
    // 选项可以拥有的参数个数(默认无上限)
    private int numberOfArgs = UNINITIALIZED;
    // 参数值(argument value)列表
    private List<String> values = new ArrayList<String>();
    private char valueSeparator;
    private Class<?> type = String.class;

    private Option(final Builder builder) {
        this.argName = builder.argName;
        this.description = builder.description;
        this.longOpt = builder.longOpt;
        this.numberOfArgs = builder.numberOfArgs;
        this.opt = builder.opt;
        this.optionalArg = builder.optionalArg;
        this.required = builder.required;
        this.type = builder.type;
        this.valueSeparator = builder.valueSeparator;
    }

    // ------------------------------------------------------------------------------------------------------------------
    /**
     * 构造函数
     * 
     * @param opt
     * @param description
     * @throws IllegalArgumentException
     */
    public Option(final String opt, final String description) throws IllegalArgumentException {
        this(opt, null, false, description);
    }

    public Option(final String opt, final boolean hasArg, final String description) throws IllegalArgumentException {
        this(opt, null, hasArg, description);
    }

    public Option(final String opt, final String longOpt, final boolean hasArg, final String description)
            throws IllegalArgumentException {
        OptionValidator.validateOption(opt);

        this.opt = opt;
        this.longOpt = longOpt;

        if (hasArg) {
            this.numberOfArgs = 1;
        }

        this.description = description;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getLongOpt() {
        return longOpt;
    }

    public void setLongOpt(String longOpt) {
        this.longOpt = longOpt;
    }

    public String getArgName() {
        return argName;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean hasOptionalArg() {
        return optionalArg;
    }

    public void setOptionalArg(boolean optionalArg) {
        this.optionalArg = optionalArg;
    }

    public int getArgs() {
        return numberOfArgs;
    }

    public void setArgs(int numberOfArgs) {
        this.numberOfArgs = numberOfArgs;
    }

    public Object getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public char getValuesep() {
        return valueSeparator;
    }

    public void setValueSeparator(char valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public boolean hasLongOpt() {
        return longOpt != null;
    }

    public boolean hasArgName() {
        return argName != null && argName.length() > 0;
    }

    public boolean hasValueSeparator() {
        return valueSeparator > 0;
    }

    /**
     * 判断选项参数个数是否与期望的参数个数一致
     * @return
     */
    public boolean requiresArg() {

        // 可选参数 不提供也可以
        if (optionalArg) {
            return false;
        }

        // 参数个数无上限 但至少需要一个 如果参数列表为空表示还需要参数
        if (numberOfArgs == UNLIMITED_VALUES) {
            return values.isEmpty();
        }

        // 是否可以接收更多参数
        return acceptsArg();

    }

    /**
     * 判断选项是否可以接受更多参数 当达到参数上限返回false
     *
     * @return
     */
    public boolean acceptsArg() {
        return (hasArg() || hasArgs() || hasOptionalArg()) && (numberOfArgs <= 0 || values.size() < numberOfArgs);
    }

    /**
     * 可以拥有至少1个参数
     * @return
     */
    public boolean hasArg() {
        return numberOfArgs > 0 || numberOfArgs == UNLIMITED_VALUES;
    }

    /**
     * 可以拥有至少2个参数
     * @return
     */
    public boolean hasArgs() {
        return numberOfArgs > 1 || numberOfArgs == UNLIMITED_VALUES;
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getKey() {
        return (opt == null) ? longOpt : opt;
    }



    public void addValueForProcessing(final String value) {
        if (numberOfArgs == UNINITIALIZED) {
            throw new RuntimeException("NO_ARGS_ALLOWED");
        }
        processValue(value);
    }

    private void processValue(String value) {
        // this Option has a separator character
        if (hasValueSeparator()) {
            // get the separator character
            final char sep = getValueSeparator();

            // store the index for the value separator
            int index = value.indexOf(sep);

            // while there are more value separators
            while (index != -1) {
                // next value to be added
                if (values.size() == numberOfArgs - 1) {
                    break;
                }

                // store
                add(value.substring(0, index));

                // parse
                value = value.substring(index + 1);

                // get new index
                index = value.indexOf(sep);
            }
        }

        // store the actual value or the last value that has been parsed
        add(value);
    }

    public char getValueSeparator() {
        return valueSeparator;
    }

    private void add(final String value) {
        if (!acceptsArg()) {
            throw new RuntimeException("Cannot add value, list full.");
        }

        values.add(value);
    }

    public String getValue() {
        return hasNoValues() ? null : values.get(0);
    }

    public String getValue(final int index) throws IndexOutOfBoundsException {
        return hasNoValues() ? null : values.get(index);
    }

    public String getValue(final String defaultValue) {
        final String value = getValue();

        return (value != null) ? value : defaultValue;
    }

    public String[] getValues() {
        return hasNoValues() ? null : values.toArray(new String[values.size()]);
    }

    public List<String> getValuesList() {
        return values;
    }

    private boolean hasNoValues() {
        return values.isEmpty();
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static Builder builder(final String opt) {
        return new Builder(opt);
    }

    /**
     * Option选项构造器
     */
    public static final class Builder {
        private final String opt;
        private String description;
        private String longOpt;
        private String argName;
        private boolean required;
        private boolean optionalArg;
        private int numberOfArgs = UNINITIALIZED;
        private Class<?> type = String.class;
        private char valueSeparator;

        private Builder(final String opt) throws IllegalArgumentException {
            OptionValidator.validateOption(opt);
            this.opt = opt;
        }

        public Builder argName(final String argName) {
            this.argName = argName;
            return this;
        }

        public Builder desc(final String description) {
            this.description = description;
            return this;
        }

        public Builder longOpt(final String longOpt) {
            this.longOpt = longOpt;
            return this;
        }

        public Builder numberOfArgs(final int numberOfArgs) {
            this.numberOfArgs = numberOfArgs;
            return this;
        }

        public Builder optionalArg(final boolean isOptional) {
            this.optionalArg = isOptional;
            return this;
        }

        public Builder required() {
            return required(true);
        }

        public Builder required(final boolean required) {
            this.required = required;
            return this;
        }

        public Builder type(final Class<?> type) {
            this.type = type;
            return this;
        }

        // 选项使用=分割参数与值
        public Builder valueSeparator() {
            return valueSeparator('=');
        }

        // 设定选项分隔符-Dkey${sep}value
        public Builder valueSeparator(final char sep) {
            valueSeparator = sep;
            return this;
        }

        // 设置该选项含有参数
        public Builder hasArg() {
            return hasArg(true);
        }

        // 设置该选项是否含有参数
        public Builder hasArg(final boolean hasArg) {
            numberOfArgs = hasArg ? 1 : Option.UNINITIALIZED;
            return this;
        }

        // 设置该选项含有无上限的参数值
        public Builder hasArgs() {
            numberOfArgs = Option.UNLIMITED_VALUES;
            return this;
        }

        public Option build() {
            if (opt == null && longOpt == null) {
                throw new IllegalArgumentException("Either opt or longOpt must be specified");
            }
            return new Option(this);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------

}
