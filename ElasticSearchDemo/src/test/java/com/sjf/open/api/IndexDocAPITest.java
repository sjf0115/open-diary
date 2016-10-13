package com.sjf.open.api;


import com.google.common.collect.Maps;
import com.sjf.open.common.Common;
import com.sjf.open.model.FootballPlayer;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xiaosi on 16-10-10.
 */
public class IndexDocAPITest {

    private static final Logger logger = LoggerFactory.getLogger(IndexDocAPITest.class);

    private Client client = Common.createClient();

    @Test
    public void indexDocByXContentBuilder() throws Exception {
        String index = "football-index";
        String type = "football-type";
        String id = "4";

        XContentBuilder xContentBuilder;
        try {
            xContentBuilder = XContentFactory.jsonBuilder();
            xContentBuilder
                    .startObject()
                        .field("name", "托雷斯")
                        .field("club", "马德里竞技俱乐部")
                        .field("country", "西班牙")
                    .endObject();

        } catch (IOException e) {
            logger.error("----------indexDocByXContentBuilder create xContentBuilder failed", e);
            return;
        }

        boolean result = IndexDocAPI.indexDocByXContentBuilder(client, index, type, id, xContentBuilder);
        logger.info("--------- indexDocByXContentBuilder result {}", result);
    }

    @Test
    public void indexDocByBean() throws Exception {

        String index = "football-index";
        String type = "football-type";
        String id = "3";

        // 具体插入什么插入数据 取决于索引和类型结构 例如下面的age不会被插入 索引中不存在该字段
        FootballPlayer footballPlayer = new FootballPlayer();
        footballPlayer.setName("卡卡");
        footballPlayer.setAge(35);
        footballPlayer.setClub("奥兰多城俱乐部");
        footballPlayer.setCountry("巴西");

        boolean result = IndexDocAPI.indexDocByBean(client, index, type , id,  footballPlayer);
        logger.info("--------- indexDocByBean {}", result);
    }

    @Test
    public void indexDocByMap() throws Exception {

        String index = "football-index";
        String type = "football-type";
        String id = "2";

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "穆勒");
        map.put("club", "拜仁慕尼黑俱乐部");
        map.put("country", "德国");

        boolean result = IndexDocAPI.indexDocByMap(client, index, type , id,  map);
        logger.info("--------- indexDocByMap {}", result);

    }

    @Test
    public void indexDocByJSON() throws Exception {
        String index = "football-index";
        String type = "football-type";
        String id = "1";
        String json = "{" +
                "\"club\":\"巴萨罗那俱乐部\"," +
                "\"country\":\"阿根廷\"," +
                "\"name\":\"梅西\"" +
                "}";
        boolean result = IndexDocAPI.indexDocByJSON(client, index, type , id,  json);
        logger.info("--------- indexDocByJSON {}", result);
    }

    @Test
    public void bulkRequest() throws Exception {

    }

}