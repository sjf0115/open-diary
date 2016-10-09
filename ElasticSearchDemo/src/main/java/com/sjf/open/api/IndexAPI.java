package com.sjf.open.api;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.sjf.open.common.Common.createClient;
/**
 * Created by xiaosi on 16-9-27.
 */
public class IndexAPI {

    private static final Logger logger = LoggerFactory.getLogger(IndexAPI.class);

    /**
     * 创建索引
     * @param client
     * @param index
     * @return
     */
    public static boolean createIndex(Client client, String index){

        CreateIndexResponse response = client.admin().indices().prepareCreate(index).execute().actionGet();
        return response.isAcknowledged();

    }

    /**
     * 删除索引
     * @param client
     * @param index
     */
    public static boolean deleteIndex(Client client, String index) {

        DeleteIndexResponse response = client.admin().indices().prepareDelete(index).execute().actionGet();
        return response.isAcknowledged();

    }

    public static void main(String[] args) {

        Client client = createClient();
        try {
            deleteIndex(client, "student-index");
        } catch (Exception e) {
            logger.error("-------- ", e);
        }
        finally {
            client.close();
        }

    }

}
