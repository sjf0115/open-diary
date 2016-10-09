package com.sjf.open.api;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.sjf.open.common.Common.createClient;
/**
 * Created by xiaosi on 16-9-27.
 */
public class IndexAPI {

    private static final Logger logger = LoggerFactory.getLogger(IndexAPI.class);


    public static boolean isIndexExists(Client client, String indexName) {

        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(indexName);

        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(indicesExistsRequest).actionGet();

        if (inExistsResponse.isExists()) {
            return true;
        }
        return false;
    }

    /**
     * 创建空索引  默认setting 无mapping
     * @param client
     * @param index
     * @return
     */
    public static boolean createIndex(Client client, String index){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index).get();
        return response.isAcknowledged();

    }

    /**
     * 创建索引 指定setting
     * @param client
     * @param index
     * @return
     */
    public static boolean createIndexBySetting(Client client, String index){

        Settings settings = Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2).build();
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index).setSettings(settings).get();
        System.out.println(" ---------" + response.toString());
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
            //createIndexBySetting(client, "stu-index");
            deleteIndex(client, "stu-index");
        } catch (Exception e) {
            logger.error("-------- ", e);
        }
        finally {
            client.close();
        }

    }

}
