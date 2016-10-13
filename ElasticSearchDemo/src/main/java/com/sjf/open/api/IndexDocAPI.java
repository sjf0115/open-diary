package com.sjf.open.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.sjf.open.model.Student;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xiaosi on 16-7-4.
 *
 * 索引文档
 *
 */
public class IndexDocAPI {

    private static final Logger logger = LoggerFactory.getLogger(IndexDocAPI.class);


    /**
     * 使用帮助类XContentBuilder 产生JSON 索引文档
     * @param client
     * @param index
     * @param type
     * @param id
     * @param xContentBuilder
     * @return
     */
    public static boolean indexDocByXContentBuilder(Client client, String index, String type, String id, XContentBuilder xContentBuilder) {

        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
        indexRequestBuilder.setIndex(index);
        indexRequestBuilder.setType(type);
        indexRequestBuilder.setId(id);
        indexRequestBuilder.setSource(xContentBuilder);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse response = indexRequestBuilder.get();
        return response.isCreated();

    }

    /**
     * 利用Json序列化 产生JSON 索引文档
     * 
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static boolean indexDocByBean(Client client, String index, String type, String id, Object bean) {

        // Bean转换为字节
        ObjectMapper mapper = new ObjectMapper();
        byte[] json;
        try {
            json = mapper.writeValueAsBytes(bean);
        } catch (JsonProcessingException e) {
            logger.error("---------- json 转换失败 Bean:{}", bean.toString());
            return false;
        }

        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
        indexRequestBuilder.setIndex(index);
        indexRequestBuilder.setType(type);
        indexRequestBuilder.setId(id);
        indexRequestBuilder.setSource(json);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse response = indexRequestBuilder.get();
        return response.isCreated();
    }

    /**
     *  使用Map 产生JSON 索引文档
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static boolean indexDocByMap(Client client, String index, String type, String id, Map<String, Object> map) {

        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
        indexRequestBuilder.setIndex(index);
        indexRequestBuilder.setType(type);
        indexRequestBuilder.setId(id);
        indexRequestBuilder.setSource(map);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse indexResponse = indexRequestBuilder.get();
        return indexResponse.isCreated();

    }

    /**
     *  手动方式 产生JSON 索引文档
     * @param client
     * @param index
     * @param type
     * @param id
     * @param json
     */
    public static boolean indexDocByJSON(Client client, String index, String type, String id, String json) {

        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
        indexRequestBuilder.setIndex(index);
        indexRequestBuilder.setType(type);
        indexRequestBuilder.setId(id);
        indexRequestBuilder.setSource(json);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse indexResponse = indexRequestBuilder.get();
        return indexResponse.isCreated();

    }

    /**
     * documet 之 bulk
     * @param client
     * @param index
     * @param type
     */
    public static void bulkRequest(Client client, String index, String type){
        BulkRequestBuilder bulkRequest = client.prepareBulk();
    }
}
