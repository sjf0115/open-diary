package com.sjf.open.util;

import com.sjf.open.businessType.ParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaosi on 16-7-29.
 */
public class RegexUtil {

    private static Logger logger = LoggerFactory.getLogger(RegexUtil.class);

    /**
     *
     * @param pattern
     * @param pramas
     * @return
     */
    public static boolean isMatch(Pattern pattern, String pramas) {
        try {
            Matcher matcher = pattern.matcher(pramas);
            if (matcher.find()) {
                return true;
            }//if
        } catch (Exception e) {
            logger.error("---------- RegexUtil --- isMatch {}", e);
        }
        return false;
    }

    /**
     *
     * @param pattern
     * @param group
     * @param pramas
     * @return
     */
    public static String getGroupValue(Pattern pattern, int group, String pramas) {
        try {
            Matcher matcher = pattern.matcher(pramas);
            if (matcher.find()) {
                return matcher.group(group);
            }
        } catch (Exception e) {
            logger.error("---------- RegexUtil --- getGroupValue {}", e);
        }
        return "";
    }
}
