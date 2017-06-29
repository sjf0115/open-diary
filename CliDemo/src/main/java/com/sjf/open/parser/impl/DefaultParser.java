package com.sjf.open.parser.impl;

import com.sjf.open.bean.CommandLine;
import com.sjf.open.bean.Option;
import com.sjf.open.bean.OptionGroup;
import com.sjf.open.bean.Options;
import com.sjf.open.exception.AmbiguousOptionException;
import com.sjf.open.exception.MissingArgumentException;
import com.sjf.open.exception.ParseException;
import com.sjf.open.exception.UnrecognizedOptionException;
import com.sjf.open.parser.CommandLineParser;
import com.sjf.open.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xiaosi on 17-6-29.
 */
public class DefaultParser implements CommandLineParser {

    protected String currentToken;
    protected CommandLine cmd;
    protected Options options;
    protected Option currentOption;
    protected List expectedOpts;
    // 如何处理无法识别的Token true表示停止解析并将剩余的Token添加到args列表 false抛出异常
    protected boolean stopAtNonOption;
    // 不再解析Token并简单地添加为命令行的参数
    protected boolean skipParsing;

    @Override
    public CommandLine parse(Options options, String[] arguments) throws ParseException {
        return null;
    }

    @Override
    public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return null;
    }

    /**
     * 解析
     * 
     * @param options
     * @param arguments
     * @param properties
     * @param stopAtNonOption
     * @return
     * @throws ParseException
     */
    public CommandLine parse(final Options options, final String[] arguments, final Properties properties,
            final boolean stopAtNonOption) throws ParseException {

        this.options = options;
        this.stopAtNonOption = stopAtNonOption;

        skipParsing = false;
        currentOption = null;
        expectedOpts = new ArrayList(options.getRequiredOptions());

        for (OptionGroup group : options.getOptionGroups()) {
            group.setSelected(null);
        }

        cmd = new CommandLine();

        if (arguments != null) {
            for (String argument : arguments) {
                handleToken(argument);
            }
        }

        return cmd;
    }

    /**
     * 解析Token
     * 
     * @param token 传递过来的参数列表中的一个参数
     * @throws ParseException
     */
    private void handleToken(String token) throws ParseException {

        currentToken = token;

        if (skipParsing) {
            cmd.addArg(token);
        } else if ("--".equals(token)) {
            skipParsing = true;
        } else if (currentOption != null && currentOption.acceptsArg() && isArgument(token)) {
            currentOption.addValueForProcessing(Util.stripLeadingAndTrailingQuotes(token));
        }
        // 长选项
        else if (token.startsWith("--")) {
            handleLongOption(token);
        }
        // 短选项
        else if (token.startsWith("-") && !"-".equals(token)) {
            // handleShortAndLongOption(token);
        }
        // 其他选项
        else {
            handleUnknownToken(token);
        }

        if (currentOption != null && !currentOption.acceptsArg()) {
            currentOption = null;
        }
    }

    /**
     * 处理长选项:
     *
     * --L --L=V --L V --l
     *
     * @param token
     */
    private void handleLongOption(String token) throws ParseException {

        if (token.indexOf('=') == -1) {
            handleLongOptionWithoutEqual(token);
        } else {
            handleLongOptionWithEqual(token);
        }

    }

    /**
     * 处理不带=的长选项:
     *
     * --L -L --l -l
     *
     * @param token
     */
    private void handleLongOptionWithoutEqual(String token) throws ParseException {

        // 匹配结果
        List<String> matchingOpts = options.getMatchingOptions(token);
        // 0个
        if (matchingOpts.isEmpty()) {
            // handleUnknownToken(token); ?
            handleUnknownToken(currentToken);
        }
        // 多个
        else if (matchingOpts.size() > 1) {
            throw new AmbiguousOptionException(token, matchingOpts);
        }
        // 1个
        else {
            // 圈定最可能匹配的一个选项
            handleOption(options.getOption(matchingOpts.get(0)));
        }

    }

    /**
     * 处理带=的长选项:
     *
     * --L=V -L=V --l=V -l=V
     *
     * @param token
     */
    private void handleLongOptionWithEqual(String token) throws ParseException {

        int pos = token.indexOf('=');
        String value = token.substring(pos + 1);
        String opt = token.substring(0, pos);

        // 匹配结果
        List<String> matchingOpts = options.getMatchingOptions(opt);
        // 0个
        if (matchingOpts.isEmpty()) {
            handleUnknownToken(currentToken);
        }
        // 多个
        else if (matchingOpts.size() > 1) {
            throw new AmbiguousOptionException(opt, matchingOpts);
        }
        // 1个
        else {
            Option option = options.getOption(matchingOpts.get(0));
            // 可以接受参数
            if (option.acceptsArg()) {
                handleOption(option);
                currentOption.addValueForProcessing(value);
                currentOption = null;
            } else {
                handleUnknownToken(currentToken);
            }
        }
    }

    /**
     * 处理无法识别的情况
     * 
     * @param token
     * @throws ParseException
     */
    private void handleUnknownToken(String token) throws ParseException {

        if (token.startsWith("-") && token.length() > 1 && !stopAtNonOption) {
            throw new UnrecognizedOptionException("Unrecognized option: " + token, token);
        }

        cmd.addArg(token);
        if (stopAtNonOption) {
            skipParsing = true;
        }

    }

    /**
     * 处理选项
     * 
     * @param option
     * @throws ParseException
     */
    private void handleOption(Option option) throws ParseException {

        checkRequiredArgs();

        // option = (Option) option.clone();

        // updateRequiredOptions(option);

        // cmd.addOption(option);

        if (option.hasArg()) {
            currentOption = option;
        } else {
            currentOption = null;
        }
    }

    // ------------------------------------------------------------------------------------------------------------------

    /**
     * 如果当前选项没有接收到期望的参数个数抛出异常
     * @throws ParseException
     */
    private void checkRequiredArgs() throws ParseException {

        // 参数不够抛出异常
        if (currentOption != null && currentOption.requiresArg()) {
            throw new MissingArgumentException(currentOption);
        }

    }

    // ------------------------------------------------------------------------------------------------------------------

    private boolean isArgument(final String token) {
        return !isOption(token) || isNegativeNumber(token);
    }

    private boolean isNegativeNumber(final String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private boolean isOption(final String token) {
        return isLongOption(token) || isShortOption(token);
    }

    private boolean isLongOption(final String token) {
        if (!token.startsWith("-") || token.length() == 1) {
            return false;
        }

        final int pos = token.indexOf("=");
        final String t = pos == -1 ? token : token.substring(0, pos);

        if (!options.getMatchingOptions(t).isEmpty()) {
            return true;
        } else if (getLongPrefix(token) != null && !token.startsWith("--")) {
            return true;
        }

        return false;
    }

    private String getLongPrefix(final String token) {
        final String t = Util.stripLeadingHyphens(token);

        int i;
        String opt = null;
        for (i = t.length() - 2; i > 1; i--) {
            final String prefix = t.substring(0, i);
            if (options.hasLongOption(prefix)) {
                opt = prefix;
                break;
            }
        }

        return opt;
    }

    private boolean isShortOption(final String token) {
        if (!token.startsWith("-") || token.length() == 1) {
            return false;
        }

        final int pos = token.indexOf("=");
        final String optName = pos == -1 ? token.substring(1) : token.substring(1, pos);
        if (options.hasShortOption(optName)) {
            return true;
        }
        return optName.length() > 0 && options.hasShortOption(String.valueOf(optName.charAt(0)));
    }

    // ------------------------------------------------------------------------------------------------------------------

}
