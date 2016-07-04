package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by xiaosi on 16-7-4.
 */
public class Document {
    private static final Logger logger = LoggerFactory.getLogger(Document.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "student";

    /**
     *
     * @param client
     * @param index
     * @param type
     * @param id
     */
    private static void put(Client client, String index, String type, String id) {
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
            xContentBuilder.startObject().field("name", "王俊辉").field("sex", "girl").field("age", 21)
                    .field("college","电子工程学院").field("school", "中国科技大学").endObject();

            // Index
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type, id);
            indexRequestBuilder.setSource(xContentBuilder);
            indexRequestBuilder.setTTL(8000);

            // 执行
            IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();

            logger.info("----------put {}", indexResponse.toString());
        } catch (IOException e) {
            logger.error("----------put fail {} ", e);
        }
    }

    public static void main(String[] args) {
        Client client = Common.createClient();
        put(client,INDEX,TYPE,"4");
        client.close();
    }
}
