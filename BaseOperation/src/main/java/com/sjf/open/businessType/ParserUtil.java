package com.sjf.open.businessType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sjf.open.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by xiaosi on 16-7-29.
 */
public class ParserUtil {
    private static Logger logger = LoggerFactory.getLogger(ParserUtil.class);

    private static String Q_SEARCH_PREFIX = "\\[(\\d*\\s\\d*\\s\\d*:\\d*:\\d*)\\]>>\\s([^:]*):(.*)";
    private static String UID_REG = "(&|^)uid=([^&]*)";
    private static String BSTATUS_REG = "(&|^)bstatus=([^&]*)";
    private static String TRACE_ID_REG = "(&|^)traceId=([^&|:]*)";
    private static String URL_REG = "(http://[^?]*)";

    private static Pattern qSearchPattern = Pattern.compile(Q_SEARCH_PREFIX);
    private static Pattern uidPattern = Pattern.compile(UID_REG);
    private static Pattern bstatusPattern = Pattern.compile(BSTATUS_REG);
    private static Pattern traceIDPattern = Pattern.compile(TRACE_ID_REG);
    private static Pattern urlPattern = Pattern.compile(URL_REG);

    private static final String queryUrl1 = "http://hotel.qunar.com/render/renderMAPI.jsp";
    private static final String queryUrl2 = "http://hotelsearch.wap.qunar.com/query";
    private static final String queryUrl3 = "http://api.map.baidu.com/";
    private static final String queryUrl4 = "http://hotelprice.corp.qunar.com/price/mapi.jsp";
    private static final String queryUrl5 = "http://mhs.corp.qunar.com/hotel-tts/v2/submit";
    private static final String queryUrl6 = "http://mhs.corp.qunar.com/hotel-tts/v2/preSubmit";

    // private static List<String> includePathList =
    // Lists.newArrayList("QueryHotelList","SrvLastMinList","h_detailRecList","hotelRecommend","lhotel","mhotel","QueryHotelDetail","SrvLastMinDetail","QueryHotelDetailPad","hdetailprice","SrvLastMinDetailTWell","lastmindetailprice","SrvPreOrderGet","SrvHotelOtaTtsPreOrderGet","SrvLogger30","SrvLogger25",
    // "SrvHotelOtaTtsSubmitOrder","SrvHotelBook" ,"QueryHotelListPad");

    private static void hotelParser(String logLine) {
        boolean isQSearchLog = RegexUtil.isMatch(qSearchPattern, logLine);
        if (isQSearchLog) {
            qSearchParser(logLine);
        } // if
        else {
            qSearchParser2(logLine);
        }
    }

    /**
     * qSearchParser
     * 
     * @param logLine
     */
    private static void qSearchParser(String logLine) {
        String time = RegexUtil.getGroupValue(qSearchPattern, 1, logLine);
        String action = RegexUtil.getGroupValue(qSearchPattern, 2, logLine);
        String params = RegexUtil.getGroupValue(qSearchPattern, 3, logLine);

        logger.info("------------------ time {} action {} params {}", time, action, params);

        // action 是否在指定范围之内
        if (!isRightPath(action)) {
            return;
        }

        // uid
        String uid = "";
        if (RegexUtil.isMatch(uidPattern, params)) {
            uid = RegexUtil.getGroupValue(uidPattern, 2, params);
            logger.info("------------------ uid {}", uid);
        }

        // 剔除罗涛测试设备号
        if (!isRightDevice(uid)) {
            return;
        }

        // bstatus
        String bstatus = "";
        if (RegexUtil.isMatch(bstatusPattern, params)) {
            bstatus = RegexUtil.getGroupValue(bstatusPattern, 2, params);
            logger.info("------------------ bstatus {}", bstatus);
        }
        if (!"0".equals(bstatus)) {
            return;
        }

        // traceID
        String traceID = "";
        if (RegexUtil.isMatch(traceIDPattern, params)) {
            traceID = RegexUtil.getGroupValue(traceIDPattern, 2, params);
            logger.info("------------------ traceID {}", traceID);
        }
    }

    /**
     * action 是否在指定范围之内
     * 
     * @param action
     * @return
     */
    private static boolean isRightPath(String action) {
        if (!"QueryHotelList".equals(action) && !"SrvLastMinList".equals(action) && !"h_detailRecList".equals(action)
                && !"hotelRecommend".equals(action) && !"lhotel".equals(action) && !"mhotel".equals(action)
                && !"QueryHotelDetail".equals(action) && !"SrvLastMinDetail".equals(action)
                && !"QueryHotelDetailPad".equals(action) && !"hdetailprice".equals(action)
                && !"SrvLastMinDetailTWell".equals(action) && !"lastmindetailprice".equals(action)
                && !"SrvPreOrderGet".equals(action) && !"SrvHotelOtaTtsPreOrderGet".equals(action)
                && !"SrvLogger30".equals(action) && !"SrvLogger25".equals(action)
                && !"SrvHotelOtaTtsSubmitOrder".equals(action) && !"SrvHotelBook".equals(action)
                && !"QueryHotelListPad".equals(action)) {
            return false;
        }
        return true;
    }

    /**
     * 剔除罗涛测试设备号
     * 
     * @param uid
     * @return
     */
    private static boolean isRightDevice(String uid) {
        if ("575107cd-8df7-5cd9-95b6-9a1bbb341d5d".equals(uid) || "0".equals(uid) || "4999010640000".equals(uid)
                || "359133041522729".equals(uid) || "unknown".equals(uid) || "111111111111111".equals(uid)
                || "353163056681595".equals(uid) || "0123456789abcdef".equals(uid) || "355533057468077".equals(uid)
                || "abfe9c3a6cc9724d1213baba66b2ecbb88343956".equals(uid)
                || "5333cfb17d24753df8e9c27bcaf1c8c52b243f40".equals(uid)
                || "71e31260a361a8c5ae68df423c72b0be92e1d6ca".equals(uid)
                || "f494c25f1e2a84c3f56886a982520ac5e4d90885".equals(uid)
                || "a3871121f140e54fbe53bbde7e4c32b364db4ec0".equals(uid)
                || "b1480b44fcd6432a427c94f72877310bb01201cc".equals(uid) || "68:09:27:d4:0f:55".equals(uid)
                || "3b0ec6fc7ef427c114ce95bd6da9631133208ecb".equals(uid)) {
            return false;
        }
        return true;
    }

    /**
     * xxx####xxx###
     * 
     * @param logLine
     */
    private static void qSearchParser2(String logLine) {
        String[] array = logLine.split("####");
        int size = array.length;

        String time = array[0];

        // traceID
        String traceID = array[1];
        traceID = traceID.split("-")[traceID.split("-").length - 1];
        if (traceID != null && !traceID.equals("")) {
            if (traceID.endsWith(":")) {
                traceID = traceID.substring(0, traceID.length() - 1);
            }
            logger.info("--------- traceID {}", traceID);
        }

        // url
        String url = array[2];
        String requesthost = RegexUtil.getGroupValue(urlPattern, 1, url);
        if(!isRightUrl(requesthost)){
            return;
        }

        // url
        if(size > 6){
            String uid = array[6];
            if(!isRightDevice(uid)){
                return;
            }
        }

        // pid
        if(size > 8){
            String pid = array[8];
            if (pid == null || pid.equals("10020")) {
                return;
            }
        }

        // backData
        if(size > 5){
            String backData = array[5];
            if (backData == null || "".equals(backData)) {
                return;
            }

            String ids = "";
            if(queryUrl4.equals(requesthost)){
                ids = analysisDetailData(backData);
            }
            else if(!queryUrl5.equals(requesthost) && !queryUrl6.equals(requesthost)){
                ids = analysisBackData(backData);
            }
            logger.info("-------- ids {}", ids);
        }
    }

    /**
     * url是否在指定范围之内
     * 
     * @param requesthost
     * @return
     */
    private static boolean isRightUrl(String requesthost) {
        if (!"http://hotel.qunar.com/render/renderMAPI.jsp".equals(requesthost)
                && !"http://hotelsearch.wap.qunar.com/query".equals(requesthost)
                && !"http://hotelprice.corp.qunar.com/price/mapi.jsp".equals(requesthost)
                && !"http://mhs.corp.qunar.com/hotel-tts/v2/submit".equals(requesthost)
                && !"http://mhs.corp.qunar.com/hotel-tts/v2/preSubmit".equals(requesthost)) {
            return false;
        }
        return true;
    }

    /**
     * 分析backData
     * @param backData
     * @return
     */
    private static String analysisDetailData(String backData) {
        StringBuilder sb = new StringBuilder();
        String ids = "";
        String minPrice = String.valueOf(Integer.MAX_VALUE);
        try {
            JsonObject jsonObject = new JsonParser().parse(backData).getAsJsonObject();
            JsonObject hotels = jsonObject.getAsJsonObject("hotels");
            for (Map.Entry<String, JsonElement> stringJsonElementEntry : hotels.entrySet()) {
                ids = stringJsonElementEntry.getKey();
                JsonArray mobilePrice = stringJsonElementEntry.getValue().getAsJsonObject()
                        .getAsJsonArray("mobilePrices");
                for (JsonElement jsonElement : mobilePrice) {
                    String price = jsonElement.getAsJsonObject().get("price").getAsString();
                    if (Double.valueOf(price).compareTo(Double.valueOf(minPrice)) == -1) {
                        minPrice = price;
                    }
                }
            }
            sb.append(ids).append("#").append(minPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 分析backData
     * @param backData
     * @return
     */
    private static String analysisBackData(final String backData) {
        String rtn = "";
        StringBuilder sb = new StringBuilder();
        try {
            JsonObject jsonObject = new JsonParser().parse(backData).getAsJsonObject();
            JsonArray hotels = jsonObject.getAsJsonArray("hotels");
            for (int i = 0; i < hotels.size(); i++) {
                JsonObject obj = hotels.get(i).getAsJsonObject();
                final String id = obj.get("id").getAsString();
                final String price = obj.get("mobilePrice").getAsString();
                sb.append(id).append("#").append(price).append("&");
            }
            rtn = sb.substring(0, sb.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public static void main(String[] args) {
        // String str = "[07 25 22:59:22]>>
        // SrvUidBlackListService:t=invalidRequest-flightlowprice:cid=C2404&uid=03005AFD08008E750500F843060001000400D846040006600400A6A90100B8D70200501D09007C7F&oldvid=15001002&vid=15001002&pid=10010&cp=2&key=636050843655039023&avers=2&ip=14.114.9.171&param={\"c\":\"北京\"}&toClientSize=176&bstatus=22335&qsearchTotalTime=1&qsearchTime=1&traceId=1469458762YaWgLQhyMg8";
        String str = "2016-07-31 00:00:00,001####SERVICE_HOTEL_REDENVELOP-3018-1469894399CQ2Jz29slL8_h_hRedEnvelopeQuery####23####http://ecoupon.corp.qunar.com/api/redpacket/query.htm?userId=216231747&status=2&uid=B5952CFB-F71C-4FF6-B60F-3AF1DD1CF5C4&gid=78343534-E36F-3CE2-3F40-3FBB4F892297&type=QFHL####200####{\"data\":{\"list\":[{\"blackHotelList\":[],\"cornerLabel\":\"\",\"createTime\":1468771838000,\"firstUsed\":true,\"flag\":21,\"fromDate\":\"2016-07-18\",\"gid\":\"78343534-E36F-3CE2-3F40-3FBB4F892297\",\"id\":10898093,\"isBuy\":false,\"isDevice\":true,\"lowestPrice\":0,\"name\":\"会员红包(待激活)\",\"paramType\":2,\"paramValue\":\"300.00\",\"primaryKey\":\"e_10898093\",\"serialNo\":\"pre_rp21_obdc34971468771838127\",\"source\":9,\"status\":2,\"subtitle\":\"手机用户专享\",\"toDate\":\"2016-08-17\",\"totalMoney\":\"300\",\"touchUrl\":\"http://touch.qunar.com/h5/hotel/hotelRedEnvelope?uid=B5952CFB-F71C-4FF6-B60F-3AF1DD1CF5C4&gid=78343534-E36F-3CE2-3F40-3FBB4F892297&redId=10898093&localRed=true&redflag=21\",\"uid\":\"B5952CFB-F71C-4FF6-B60F-3AF1DD1CF5C4\",\"usableRange\":\"激活后会员红包可用于预订有<span style=\\\"background-color:red;color:#FFFFFF\\\">会员红包</span>或<span style=\\\"background-color:red;color:#FFFFFF\\\">五折大促</span>标签\n"
                + "的酒店(含钟点房)，在标有相应活动标签的房型报价使用(五折大促仅限未在去哪儿酒店下过单的新用户)；或在红包商城兑换礼品\",\"usageRule\":\"登录去哪儿网账户，按操作提示激活红包。激活后红包金额将累加至会员红包\",\"useScene\":0,\"useScope\":\"~\",\"userId\":0,\"userMobile\":\"\",\"userName\":\"\",\"userRemark\":\"\"},{\"blackHotelList\":[],\"cornerLabel\":\"\",\"firstUsed\":false,\"flag\":20,\"fromDate\":\"2016-07-31\",\"gid\":\"78343534-E36F-3CE2-3F40-3FBB4F892297\",\"id\":0,\"isBuy\":false,\"isDevice\":false,\"lowestPrice\":0,\"name\":\"会员红包\",\"paramType\":2,\"paramValue\":\"300.00\",\"primaryKey\":\"0_0\",\"serialNo\":\"\",\"source\":0,\"status\":2,\"subtitle\":\"手机用户专享\",\"toDate\":\"2016-09-30\",\"totalMoney\":\"300\",\"touchUrl\":\"http://touch.qunar.com/h5/hotel/hotelRedEnvelope?_uni=216231747&redId=0&localRed=false&redflag=20\",\"uid\":\"B5952CFB-F71C-4FF6-B60F-3AF1DD1CF5C4\",\"usableRange\":\"会员红包可用于预订有<span style=\\\"background-color:red;color:#FFFFFF\\\">会员红包</span>或<span style=\\\"background-color:red;color:#FFFFFF\\\">五折\n"
                + "大促</span>标签的酒店(含钟点房)，在标有相应活动标签的房型报价使用（五折大促仅限未在去哪儿酒店下过单的新用户）；或在红包商城兑换礼品\",\"usageRule\":\"会员红包为去哪儿旅行客户端专享。会员红包金额可\n"
                + "叠加，可拆分多次使用。如订单取消，已使用的红包金额会退回会员红包\",\"useScene\":0,\"useScope\":\"~\",\"userId\":216231747,\"userMobile\":\"\",\"userName\":\"\",\"userRemark\":\"\"}]},\"errcode\":0,\"ret\":true,\"ver\":\"1.0\"}####B5952CFB-F71C-4FF6-B60F-3AF1DD1CF5C4####80011119####10010####C1001";
        hotelParser(str);
    }
}
