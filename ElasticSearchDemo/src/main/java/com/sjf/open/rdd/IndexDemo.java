package com.sjf.open.rdd;

import com.sjf.open.api.common.ESClientBuilder;
import com.sjf.open.api.index.IndexAPI;
import com.sjf.open.api.index.IndexDocAPI;
import com.sjf.open.utils.ESUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 17-5-24.
 */
public class IndexDemo {

    private static final Logger logger = LoggerFactory.getLogger(IndexDemo.class);


    public static void putIndexMapping2(String index, String type) {

        // mapping
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(type)
                    .startObject("properties")
                    .startObject("comments").field("type", "nested")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            logger.error("--------- putIndexMapping 创建 mapping 失败：", e);
            return;
        }

        boolean result = IndexAPI.createIndex(index, type, mappingBuilder);
        if(result){
            logger.info("---------　创建索引成功");
        }
        else {
            logger.info("--------- 创建索引失败");
        }

    }

    public static void indexDocument(String index, String type){
        String json = "{\n" +
                "  \"userInfo\": {\n" +
                "    \"gid\": \"0006F0F0-A311-A176-57BE-FB7260A63024\",\n" +
                "    \"uid\": \"863116030132385\",\n" +
                "    \"vid\": \"60001166\",\n" +
                "    \"phoneType\": \"vivo X7Plus\",\n" +
                "    \"pid\": \"10010\",\n" +
                "    \"cid\": \"C2022\",\n" +
                "    \"permanentLocation\": \"台州市\"\n" +
                "  },\n" +
                "  \"businessActionsMap\": {\n" +
                "    \"flight\": {\n" +
                "      \"_type\": \"FlightActions\",\n" +
                "      \"search\": 21,\n" +
                "      \"order\": 0\n" +
                "    },\n" +
                "    \"hotel\": {\n" +
                "      \"_type\": \"HotelActions\",\n" +
                "      \"search\": 3,\n" +
                "      \"click\": 5,\n" +
                "      \"order\": 0\n" +
                "    },\n" +
                "    \"vacation\": {\n" +
                "      \"_type\": \"VacationActions\",\n" +
                "      \"search\": 1,\n" +
                "      \"click\": 0,\n" +
                "      \"order\": 0\n" +
                "    }\n" +
                "  }\n" +
                "}";
        IndexDocAPI.indexDocByJSON(index, type, "1", json);
    }

    public static void main(String[] args) {

        String index = "my-index-test";
        String type = "my-type-test";
        String json = "{\"userInfo\": {\"gid\": \"0006F0F0-A311-A176-57BE-FB7260A63024\",\"uid\": \"863116030132385\",\"vid\": \"60001166\",\"phoneType\": \"vivo X7Plus\",\"pid\": \"10010\",\"cid\": \"C2022\",\"permanentLocation\": \"台州市\"},\"businessActionsMap\": {\"flight\": {\"_type\": \"FlightActions\",\"search\": 21,\"order\": 0},\"hotel\": {\"_type\": \"HotelActions\",\"search\": 3,\"click\": 5,\"order\": 0},\"vacation\": {\"_type\": \"VacationActions\",\"search\": 1,\"click\": 0,\"order\": 0}}}";

        IndexDocAPI.indexDocByJSON(index, type, "1", json);

        ConstantScoreQueryBuilder queryBuilder = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("gid", "0006F0F0-A311-A176-57BE-FB7260A63024"));

        //NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("userInfo", QueryBuilders.termQuery("userInfo.gid", "0006F0F0-A311-A176-57BE-FB7260A63024"));

        // Search
        Client client = ESClientBuilder.builder();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 输出
        ESUtil.print(searchResponse);
    }
}
