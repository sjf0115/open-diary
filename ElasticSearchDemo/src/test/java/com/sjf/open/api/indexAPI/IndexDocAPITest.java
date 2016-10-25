package com.sjf.open.api.indexAPI;


import com.google.common.collect.Maps;
import com.sjf.open.common.Common;
import com.sjf.open.model.FootballPlayer;
import com.sjf.open.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
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
        String id = "11";

        FootballPlayer footballPlayer = new FootballPlayer();
        footballPlayer.setName("范佩西");
        footballPlayer.setClub("曼联俱乐部");
        footballPlayer.setCountry("荷兰");

        boolean result = IndexDocAPI.indexDocByBean(client, index, type , id,  footballPlayer);
        logger.info("--------- indexDocByBean {}", result);
    }

    @Test
    public void indexDocByMap() throws Exception {

        String index = "test-index";
        String type = "test-type";
        String id = "1";

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "C罗");
        map.put("sex", true);
        map.put("age", 31);
        map.put("birthday", "1985-02-05");
        map.put("club", "皇家马德里俱乐部");

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
    public void test() throws Exception {
        String index = "simple-index";
        String type = "simple-type";

        FootballPlayer footballPlayer;

        for(int i = 0;i < 100000000; i++){
            String country = "";
            while(StringUtils.isBlank(country)){
                country = RandomUtil.getRandomMixedStr(6);
            }

            String club = "";
            while(StringUtils.isBlank(club)){
                club = RandomUtil.getRandomMixedStr(5) + "俱乐部";
            }

            String name = "";
            while(StringUtils.isBlank(name)){
                name = RandomUtil.getRandomMixedStr(4);
            }


            footballPlayer = new FootballPlayer();
            footballPlayer.setName(name);
            footballPlayer.setClub(club);
            footballPlayer.setCountry(country);

            String id = i + "";

            boolean result = IndexDocAPI.indexDocByBean(client, index, type , id,  footballPlayer);
            logger.info("--------- indexDocByBean {}", result);

        }
    }

}