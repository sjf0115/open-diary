package com.sjf.open.demo;

import com.sjf.open.api.common.ESClientBuilder;
import com.sjf.open.api.index.IndexAPI;
import com.sjf.open.api.index.IndexDocAPI;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
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
                                .startObject("uid").field("type", "string").field("index", "not_analyzed").endObject()
                                .startObject("permanentLocation").field("type", "string").field("index", "not_analyzed").endObject()
                            .endObject()
                        .endObject()
                        .startObject("productID").field("type", "string").field("index", "not_analyzed").endObject()
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
                "    \"permanentLocation\": \"台州市\"\n" +
                "  },\n" +
                "\"productID\":\"dddddfsfsfsfafafsaf\"\n" +
                "}";
        IndexDocAPI.indexDocByJSON(index, type, "1", json);
    }

    public static void main(String[] args) {
        String index = "nested_index";
        String type = "nested_type";
        //putIndexMapping(index, type);
        indexDocument(index, type);
    }
}
