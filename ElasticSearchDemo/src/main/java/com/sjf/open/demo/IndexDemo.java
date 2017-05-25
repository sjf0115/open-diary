package com.sjf.open.demo;

import com.sjf.open.api.common.ESClientBuilder;
import com.sjf.open.api.index.IndexAPI;
import com.sjf.open.api.index.IndexDocAPI;
import com.sjf.open.api.query.TermQueryAPI;
import com.sjf.open.utils.ESUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.AliasOrIndex;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 17-5-24.
 */
public class IndexDemo {

    private static final Logger logger = LoggerFactory.getLogger(IndexDemo.class);

    public static void putIndexMapping(String index, String type) {

        // mapping
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(type)
                    .startObject("properties")
                        .startObject("userInfo").field("type", "nested")
                            .startObject("properties")
                                .startObject("gid").field("type", "string").field("index", "not_analyzed").endObject()
                            .endObject()
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

        putIndexMapping2("blogpost", "blogpost");

        String json = "{\n" +
                "  \"title\": \"Nest eggs\",\n" +
                "  \"body\":  \"Making your money work...\",\n" +
                "  \"tags\":  [ \"cash\", \"shares\" ],\n" +
                "  \"comments\": [ \n" +
                "    {\n" +
                "      \"name\":    \"John Smith\",\n" +
                "      \"comment\": \"Great article\",\n" +
                "      \"age\":     28,\n" +
                "      \"stars\":   4,\n" +
                "      \"date\":    \"2014-09-01\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":    \"Alice White\",\n" +
                "      \"comment\": \"More like this please\",\n" +
                "      \"age\":     31,\n" +
                "      \"stars\":   5,\n" +
                "      \"date\":    \"2014-10-22\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        IndexDocAPI.indexDocByJSON("blogpost", "blogpost", "1", json);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("comments.name", "Alice"));
        boolQueryBuilder.must(QueryBuilders.termQuery("comments.age", "31"));

        NestedQueryBuilder queryBuilder = QueryBuilders.nestedQuery("comments", boolQueryBuilder);
        // Search
        Client client = ESClientBuilder.builder();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blogpost");
        searchRequestBuilder.setTypes("blogpost");
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 输出
        ESUtil.print(searchResponse);
    }
}
