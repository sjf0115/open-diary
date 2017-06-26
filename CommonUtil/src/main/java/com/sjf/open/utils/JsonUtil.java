package com.sjf.open.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by xiaosi on 17-4-24.
 */
public class JsonUtil {
    private static final JsonParser jsonParser = new JsonParser();

    /**
     * 解析Json
     * @param json
     * @return
     */
    public static Map<String, String> parser(String json){

        Map<String, String> resultMap = Maps.newConcurrentMap();
        if(StringUtils.isBlank(json)){
            return resultMap;
        }

        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        if(Objects.equal(jsonObject, null)){
            return resultMap;
        }

        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()){
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            if(Objects.equal(element, null)){
                continue;
            }
            String value = element.getAsString();
            resultMap.put(key, value);
        }

        return resultMap;

    }

    public static void main(String[] args) {
        String json = "{\"lat\":\"31.159818\",\"gid\":\"093F63D3-9E58-0ADC-CF78-7735A103DB22\",\"marketingInfo\":\"[{\\\\activiyNo\\\\:\\\\20160426160514602848\\\\,\\\\couponNo\\\\:\\\\5df774dae450b9e3\\\\,\\\\strategyId\\\\:\\\\158\\\\}]\",\"clk\":\"ap_yud\",\"vid\":\"60001161\",\"dist_city\":\"上海\",\"uid\":\"869966028747089\",\"current_city\":\"上海\",\"utmr_t\":\"ticket_preOrder\",\"bd_source\":\"android\",\"cid\":\"C4311\",\"sightId\":\"15467\",\"t\":\"1491703359749\",\"lgt\":\"121.420083\",\"in_track\":\"a_sy_search_menpiao_28\",\"catom\":\"com.mqunar.atom.sight_70\",\"pid\":\"10010\"}";
        Map<String, String> map = parser(json);
        for(String key : map.keySet()){
            System.out.println(key + "---" + map.get(key));
        }
    }
}
