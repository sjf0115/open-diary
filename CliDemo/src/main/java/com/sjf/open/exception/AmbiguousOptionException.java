package com.sjf.open.exception;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * 歧义选项异常
 *
 * Created by xiaosi on 17-6-29.
 */
public class AmbiguousOptionException extends UnrecognizedOptionException {

    // 匹配到的选项列表
    private final Collection<String> matchingOptions;

    /**
     * 构造函数
     * @param option
     * @param matchingOptions
     */
    public AmbiguousOptionException(String option, Collection<String> matchingOptions) {
        super(createMessage(option, matchingOptions), option);
        this.matchingOptions = matchingOptions;
    }

    /**
     * 组装异常信息
     * @param option
     * @param matchingOptions
     * @return
     */
    private static String createMessage(String option, Collection<String> matchingOptions) {

        StringBuilder buf = new StringBuilder("Ambiguous option: '");
        buf.append(option);
        buf.append("'  (could be: ");

        final Iterator<String> it = matchingOptions.iterator();
        while (it.hasNext()) {
            buf.append("'");
            buf.append(it.next());
            buf.append("'");
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append(")");

        return buf.toString();

    }

    /**
     * 返回匹配到的选项列表
     * @return
     */
    public Collection<String> getMatchingOptions() {
        return matchingOptions;
    }
}
