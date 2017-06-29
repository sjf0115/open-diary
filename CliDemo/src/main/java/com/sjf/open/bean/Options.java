package com.sjf.open.bean;

import com.sjf.open.bean.Option;
import com.sjf.open.utils.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示一组Option对象 描述了命令行中可能选项
 *
 * Created by xiaosi on 17-6-29.
 */
public class Options implements Serializable {

    private final Map<String, Option> shortOpts = new LinkedHashMap<String, Option>();
    private final Map<String, Option> longOpts = new LinkedHashMap<String, Option>();
    // TODO this seems wrong
    private final List<Object> requiredOpts = new ArrayList<Object>();
    private final Map<String, OptionGroup> optionGroups = new LinkedHashMap<String, OptionGroup>();

    /**
     * 添加Option分组
     * 
     * @param group
     * @return
     */
    public Options addOptionGroup(final OptionGroup group) {
        if (group.isRequired()) {
            requiredOpts.add(group);
        }

        for (final Option option : group.getOptions()) {
            option.setRequired(false);
            addOption(option);
            optionGroups.put(option.getKey(), group);
        }

        return this;
    }

    public Collection<OptionGroup> getOptionGroups() {
        return new HashSet<OptionGroup>(optionGroups.values());
    }

    public Options addOption(final String opt, final String description) {
        addOption(opt, null, false, description);
        return this;
    }

    public Options addOption(final String opt, final boolean hasArg, final String description) {
        addOption(opt, null, hasArg, description);
        return this;
    }

    public Options addOption(final String opt, final String longOpt, final boolean hasArg, final String description) {
        addOption(new Option(opt, longOpt, hasArg, description));
        return this;
    }

    public Options addRequiredOption(final String opt, final String longOpt, final boolean hasArg,
            final String description) {
        final Option option = new Option(opt, longOpt, hasArg, description);
        option.setRequired(true);
        addOption(option);
        return this;
    }

    public Options addOption(final Option opt) {
        final String key = opt.getKey();

        // add it to the long option list
        if (opt.hasLongOpt()) {
            longOpts.put(opt.getLongOpt(), opt);
        }

        // if the option is required add it to the required list
        if (opt.isRequired()) {
            if (requiredOpts.contains(key)) {
                requiredOpts.remove(requiredOpts.indexOf(key));
            }
            requiredOpts.add(key);
        }

        shortOpts.put(key, opt);

        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------

    /**
     * 返回所有的短选项
     * 
     * @return
     */
    public Collection<Option> getOptions() {
        return Collections.unmodifiableCollection(helpOptions());
    }

    /**
     * 返回Option 优先在短选项中寻找,如果不存在则在长选项中寻找
     * 
     * @param opt
     * @return
     */
    public Option getOption(String opt) {

        opt = Util.stripLeadingHyphens(opt);

        if (shortOpts.containsKey(opt)) {
            return shortOpts.get(opt);
        }

        return longOpts.get(opt);

    }

    /**
     * 返回匹配的长选项名称 如果匹配不到返回以opt开头的选项
     * 
     * @param opt
     * @return
     */
    public List<String> getMatchingOptions(String opt) {

        opt = Util.stripLeadingHyphens(opt);

        List<String> matchingOpts = new ArrayList<String>();

        if (longOpts.keySet().contains(opt)) {
            return Collections.singletonList(opt);
        }

        for (String longOpt : longOpts.keySet()) {
            if (longOpt.startsWith(opt)) {
                matchingOpts.add(longOpt);
            }
        }

        return matchingOpts;

    }

    public List getRequiredOptions() {

        return Collections.unmodifiableList(requiredOpts);

    }

    public OptionGroup getOptionGroup(Option opt) {

        return optionGroups.get(opt.getKey());

    }

    // ------------------------------------------------------------------------------------------------------------------

    public boolean hasOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);

        return shortOpts.containsKey(opt) || longOpts.containsKey(opt);
    }

    public boolean hasLongOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);

        return longOpts.containsKey(opt);
    }

    public boolean hasShortOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);

        return shortOpts.containsKey(opt);
    }

    private List<Option> helpOptions() {
        return new ArrayList<Option>(shortOpts.values());
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();

        buf.append("[ Options: [ short ");
        buf.append(shortOpts.toString());
        buf.append(" ] [ long ");
        buf.append(longOpts);
        buf.append(" ]");

        return buf.toString();
    }
}
