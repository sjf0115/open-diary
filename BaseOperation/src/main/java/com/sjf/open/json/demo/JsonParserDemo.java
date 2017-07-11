package com.sjf.open.json.demo;

import avro.shaded.com.google.common.collect.Maps;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sjf.open.json.model.ClickDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaosi on 16-7-13.
 */
public class JsonParserDemo {
    private static final JsonParser JSONPARSER = new JsonParser();

    private static Logger logger = LoggerFactory.getLogger(JsonParserDemo.class);

    private static List<String> vacationUrl = Lists.newArrayList("//vacation/detail", "//vacation/index",
            "//visa/index", "//visa/country", "//vacation/list", "touch.dujia.qunar.com");
    private static List<String> ticketlUrl = Lists.newArrayList("//sight/main?", "touch.piao.qunar.com",
            "sight/productDetail?productId", "//sight/searchList?", "touch.piao.qunar.com");

    /**
     * 解析 SuggestionResult日志
     * 
     * @param jsonObject
     */
    private static List<ClickDetail> parserSuggestion(JsonObject jsonObject) {
        JsonElement suggestionsJsonElement = jsonObject.get("suggestions");
        JsonArray suggestionsJsonArray = suggestionsJsonElement.getAsJsonArray();

        List<ClickDetail> result = Lists.newArrayList();

        for (int i = 0; i < suggestionsJsonArray.size(); i++) {
            JsonElement jsonElement = suggestionsJsonArray.get(i);
            String jsonStr = jsonElement.toString();
            JsonObject detailJsonObject = JSONPARSER.parse(jsonStr).getAsJsonObject();
            JsonElement schemeUrlElement = detailJsonObject.get("schemeUrl");
            JsonElement typeJsonElement = detailJsonObject.get("type");
            String type = typeJsonElement.getAsString();
            String schemeUrl = schemeUrlElement.getAsString();

            logger.info("-------------------- type {} --- schemeUrl {}", type, schemeUrl);
            ClickDetail clickDetail;
            if (isValid(type, schemeUrl)) {
                logger.info("------------------------- valid type {} --- schemeUrl {}", type, schemeUrl);
                clickDetail = new ClickDetail();
                // clickDetail.setBusiness(type);
                result.add(clickDetail);
            }
        }
        return result;
    }

    /**
     * 解析HotelWord2Data 日志
     * 
     * @param jsonObject
     */
    private static List<ClickDetail> parserHotelWord2Data(JsonObject jsonObject) {

        JsonElement listsJsonElement = jsonObject.get("lists");
        JsonArray listsJsonArray = listsJsonElement.getAsJsonArray();

        List<ClickDetail> result = Lists.newArrayList();

        for (int i = 0; i < listsJsonArray.size(); i++) {
            JsonElement jsonElement = listsJsonArray.get(i);
            String jsonStr = jsonElement.toString();

            JsonObject detailJsonObject = JSONPARSER.parse(jsonStr).getAsJsonObject();

            JsonElement typeJsonElement = detailJsonObject.get("type");
            String type = typeJsonElement.getAsString();

            logger.info("-----type {}", type);

            List<ClickDetail> clickDetailList = parserItems(type, detailJsonObject);
            result.addAll(clickDetailList);
        }
        return result;
    }

    private static List<ClickDetail> parserItems(String type, JsonObject jsonObject) {
        List<ClickDetail> clickDetailList = Lists.newArrayList();
        ClickDetail clickDetail;

        JsonElement itemJsonElement = jsonObject.get("items");
        JsonArray itemsJsonArray = itemJsonElement.getAsJsonArray();
        for (int j = 0; j < itemsJsonArray.size(); j++) {
            JsonElement jsonElement = itemsJsonArray.get(j);
            String jsonStr = jsonElement.toString();
            JsonObject itemJsonObject = JSONPARSER.parse(jsonStr).getAsJsonObject();
            itemJsonObject.entrySet();
            JsonElement urlJsonElement = itemJsonObject.get("url");
            String url = urlJsonElement.getAsString();

            logger.info("------------------ type {} url {}", type, url);
            if (isValid(type, url)) {
                logger.info("------------------------- valid type {} url {}", type, url);
                clickDetail = new ClickDetail();
                // clickDetail.setBusiness(type);
                clickDetailList.add(clickDetail);
            }
        }
        return clickDetailList;
    }

    /**
     * 解析Suggestion日志
     * 
     * @param json
     */
    private static List<ClickDetail> parser(String json) {
        JsonObject jsonObject = JSONPARSER.parse(json).getAsJsonObject();
        JsonElement titleJsonElement = jsonObject.get("__rT");
        String rt = titleJsonElement.getAsString();

        List<ClickDetail> clickDetailList = Lists.newArrayList();

        logger.info("=---__rT {}", rt);
        if (Objects.equal(rt, "HotelWord2Data")) {
            clickDetailList = parserHotelWord2Data(jsonObject);
        } else if (Objects.equal(rt, "SuggestionResult")) {
            clickDetailList = parserSuggestion(jsonObject);
        }
        return clickDetailList;
    }

    private static boolean isValid(String type, String url) {
        if ((Objects.equal(type, "vacation"))) {
            for (String keyUrl : vacationUrl) {
                if (url.contains(keyUrl)) {
                    return true;
                }
            }
            return false;
        } else if (Objects.equal(type, "ticket")) {
            for (String keyUrl : ticketlUrl) {
                if (url.contains(keyUrl)) {
                    return true;
                }
            }
            return false;
        } else if (Objects.equal(type, "hotel")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String json = "[{\"biz\":\"hotel\",\"couponId\":\"S10\"},{\"biz\":\"hotel\",\"couponId\":\"A\"},{\"biz\":\"hourRoom\",\"couponId\":\"HOURROOM\"}]";
        Map<String, Object> map = Maps.newHashMap();
        JsonArray jsonArray = JSONPARSER.parse(json).getAsJsonArray();
        Iterator<JsonElement> iterator = jsonArray.iterator();
    }
}
