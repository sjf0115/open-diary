package com.sjf.open.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.sjf.open.model.Student;
import com.sjf.open.common.Common;
import com.sjf.open.utils.ConstantUtil;
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
 */
public class ImportAPI {
    private static final Logger logger = LoggerFactory.getLogger(ImportAPI.class);

    /**
     *
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static boolean put(Client client, String index, String type, String id) {
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
            xContentBuilder.startObject().field("name", "王俊辉").field("sex", "boy").field("age", 24)
                    .field("college", "软件学院").field("school", "西安电子科技大学").endObject();

            // Index
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type, id);
            indexRequestBuilder.setSource(xContentBuilder);
            indexRequestBuilder.setTTL(8000);

            // 执行
            IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();

            logger.info("----------put {}", indexResponse.toString());

            return indexResponse.isCreated();
        } catch (IOException e) {
            logger.error("----------put fail {} ", e);
        }
        return false;
    }

    /**
     * 利用Json序列化Bean插入数据
     * 
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void putByBean(Client client, String index, String type, String id) {
        // 具体插入什么插入数据，取决于索引和类型结构,例如下面的age和school不会被插入,因为索引中不存在改字段
        Student student = new Student();
        student.setAge(21);
        student.setCollege("计算机学院");
        student.setName("C罗");
        student.setSex("boy");
        student.setSchool("西安电子科技大学");

        ObjectMapper mapper = new ObjectMapper();
        // Bean转换为字节
        byte[] json;
        try {
            json = mapper.writeValueAsBytes(student);
        } catch (JsonProcessingException e) {
            logger.error("---------- json 转换失败 Bean:{}", student.toString());
            return;
        }

        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type, id);
        indexRequestBuilder.setSource(json);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();
        logger.info("----------put {}", indexResponse.toString());
    }

    /**
     *
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void putByMap(Client client, String index, String type, String id) {
        Map<String, String> map = Maps.newHashMap();
        map.put("name", "穆勒");
        map.put("sex", "boy");
        map.put("age", "25");
        map.put("college", "计算机学院ddddd");
        map.put("school", "西安电子科技大学");

        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type, id);
        indexRequestBuilder.setSource(map);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();
        logger.info("----------put {}", indexResponse.toString());
    }

    /**
     *
     * @param client
     * @param index
     * @param type
     * @param id
     * @param json
     */
    public static void putByJSON(Client client, String index, String type, String id, String json) {
        // Index
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type, id);
        indexRequestBuilder.setSource(json);
        indexRequestBuilder.setTTL(8000);

        // 执行
        IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();
        logger.info("----------put {}", indexResponse.toString());
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
