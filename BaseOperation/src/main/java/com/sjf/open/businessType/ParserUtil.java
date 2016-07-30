package com.sjf.open.businessType;

import com.sjf.open.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created by xiaosi on 16-7-29.
 */
public class ParserUtil {
    private static Logger logger = LoggerFactory.getLogger(ParserUtil.class);

    private static String QSEARCH_PREFIX = "\\[(\\d*\\s\\d*\\s\\d*:\\d*:\\d*)\\]>>\\s([^:]*):(.*)";
    private static Pattern qsearchPattern = Pattern.compile(QSEARCH_PREFIX);

    private static void hotelParser(String logLine){
        boolean isQsearchLog = RegexUtil.isMatch(qsearchPattern, logLine);
        if(isQsearchLog){
            qsearchParser(logLine);
        }//if
    }

    private static void qsearchParser(String logLine){
        String action = RegexUtil.getGroupValue(qsearchPattern, 2, logLine);
        String params = RegexUtil.getGroupValue(qsearchPattern, 3, logLine);

        logger.info("------------------ action {} params {}",action, params);
    }

    public static void main(String[] args) {
        String str = "[07 25 22:59:22]>> SrvUidBlackListService:t=invalidRequest-flightlowprice:cid=C2404&uid=03005AFD08008E750500F843060001000400D846040006600400A6A90100B8D70200501D09007C7F&oldvid=15001002&vid=15001002&pid=10010&cp=2&key=636050843655039023&avers=2&ip=14.114.9.171&param={\"c\":\"北京\"}&toClientSize=176&bstatus=22335&qsearchTotalTime=1&qsearchTime=1&traceId=1469458762YaWgLQhyMg8";
        hotelParser(str);
    }
}
