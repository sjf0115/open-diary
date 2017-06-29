package com.sjf.open.bean;

import com.sjf.open.exception.ParseException;
import com.sjf.open.utils.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xiaosi on 17-6-29.
 */
public class CommandLine implements Serializable {
    private final List<String> args = new LinkedList<String>();
    private final List<Option> options = new ArrayList<Option>();

    public CommandLine() {
        // nothing to do
    }

    public boolean hasOption(final Option opt) {
        return options.contains(opt);
    }

    public boolean hasOption(final String opt) {
        return hasOption(resolveOption(opt));
    }

    public boolean hasOption(final char opt) {
        return hasOption(String.valueOf(opt));
    }

    public Object getParsedOptionValue(final Option option) throws ParseException {
        if (option == null) {
            return null;
        }
        final String res = getOptionValue(option);
        if (res == null) {
            return null;
        }
        return null;
        //return TypeHandler.createValue(res, option.getType());
    }

    public Object getParsedOptionValue(final String opt) throws ParseException {
        return getParsedOptionValue(resolveOption(opt));
    }

    public Object getParsedOptionValue(final char opt) throws ParseException {
        return getParsedOptionValue(String.valueOf(opt));
    }

    @Deprecated
    public Object getOptionObject(final String opt)
    {
        try
        {
            return getParsedOptionValue(opt);
        }
        catch (final ParseException pe)
        {
            System.err.println("Exception found converting " + opt + " to desired type: " + pe.getMessage());
            return null;
        }
    }

    public Object getOptionObject(final char opt) {
        return getOptionObject(String.valueOf(opt));
    }

    public String getOptionValue(final Option option) {
        if (option == null) {
            return null;
        }
        final String[] values = getOptionValues(option);
        return (values == null) ? null : values[0];
    }

    public String getOptionValue(final String opt) {
        return getOptionValue(resolveOption(opt));
    }

    public String getOptionValue(final char opt) {
        return getOptionValue(String.valueOf(opt));
    }

    public String[] getOptionValues(final Option option) {
        final List<String> values = new ArrayList<String>();

        for (final Option processedOption : options) {
            if (processedOption.equals(option)) {
                values.addAll(processedOption.getValuesList());
            }
        }

        return values.isEmpty() ? null : values.toArray(new String[values.size()]);
    }

    public String[] getOptionValues(final String opt) {
        return getOptionValues(resolveOption(opt));
    }

    private Option resolveOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);
        for (final Option option : options) {
            if (opt.equals(option.getOpt())) {
                return option;
            }

            if (opt.equals(option.getLongOpt())) {
                return option;
            }

        }
        return null;
    }

    public String[] getOptionValues(final char opt) {
        return getOptionValues(String.valueOf(opt));
    }

    public String getOptionValue(final Option option, final String defaultValue) {
        final String answer = getOptionValue(option);
        return (answer != null) ? answer : defaultValue;
    }

    public String getOptionValue(final String opt, final String defaultValue) {
        return getOptionValue(resolveOption(opt), defaultValue);
    }

    public String getOptionValue(final char opt, final String defaultValue) {
        return getOptionValue(String.valueOf(opt), defaultValue);
    }

    public Properties getOptionProperties(final Option option) {
        final Properties props = new Properties();

        for (final Option processedOption : options) {
            if (processedOption.equals(option)) {
                final List<String> values = processedOption.getValuesList();
                if (values.size() >= 2) {
                    // use the first 2 arguments as the key/value pair
                    props.put(values.get(0), values.get(1));
                } else if (values.size() == 1) {
                    // no explicit value, handle it as a boolean
                    props.put(values.get(0), "true");
                }
            }
        }

        return props;
    }

    public Properties getOptionProperties(final String opt) {
        final Properties props = new Properties();

        for (final Option option : options) {
            if (opt.equals(option.getOpt()) || opt.equals(option.getLongOpt())) {
                final List<String> values = option.getValuesList();
                if (values.size() >= 2) {
                    // use the first 2 arguments as the key/value pair
                    props.put(values.get(0), values.get(1));
                } else if (values.size() == 1) {
                    // no explicit value, handle it as a boolean
                    props.put(values.get(0), "true");
                }
            }
        }

        return props;
    }

    public String[] getArgs() {
        final String[] answer = new String[args.size()];

        args.toArray(answer);

        return answer;
    }

    public List<String> getArgList() {
        return args;
    }

    public void addArg(final String arg) {
        args.add(arg);
    }

    /**
     * Add an option to the command line. The values of the option are stored.
     *
     * @param opt the processed option
     */
    protected void addOption(final Option opt) {
        options.add(opt);
    }

    /**
     * Returns an iterator over the Option members of CommandLine.
     *
     * @return an <code>Iterator</code> over the processed {@link Option} members of this {@link CommandLine}
     */
    public Iterator<Option> iterator() {
        return options.iterator();
    }

    /**
     * Returns an array of the processed {@link Option}s.
     *
     * @return an array of the processed {@link Option}s.
     */
    public Option[] getOptions() {
        final Collection<Option> processed = options;

        // reinitialise array
        final Option[] optionsArray = new Option[processed.size()];

        // return the array
        return processed.toArray(optionsArray);
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * CommandLine Builder
     */
    public static final class Builder {

        private final CommandLine commandLine = new CommandLine();

        public Builder addOption(final Option opt) {
            commandLine.addOption(opt);
            return this;
        }

        public Builder addArg(final String arg) {
            commandLine.addArg(arg);
            return this;
        }

        public CommandLine build() {
            return commandLine;
        }
    }
}
