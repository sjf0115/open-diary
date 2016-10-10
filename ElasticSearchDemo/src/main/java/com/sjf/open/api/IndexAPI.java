package com.sjf.open.api;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.stats.CommonStats;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.admin.indices.stats.ShardStats;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


/**
 * Created by xiaosi on 16-9-27.
 *
 * 索引管理
 *
 */
public class IndexAPI {

    private static final Logger logger = LoggerFactory.getLogger(IndexAPI.class);

    /**
     * 判断索引是否存在
     * @param client
     * @param index
     * @return
     */
    public static boolean isIndexExists(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesExistsResponse response = indicesAdminClient.prepareExists(index).get();
        return response.isExists();

        /* 另一种方式
        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse response = client.admin().indices().exists(indicesExistsRequest).actionGet();*/
    }

    /**
     * 判断类型是否存在
     * @param client
     * @param index
     * @param type
     * @return
     */
    public static boolean isTypeExists(Client client, String index, String type) {

        if(!isIndexExists(client, index)){
            logger.info("--------- isTypeExists 索引 [{}] 不存在",index);
            return false;
        }

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        TypesExistsResponse response = indicesAdminClient.prepareTypesExists(index).setTypes(type).get();
        return response.isExists();

    }

    /**
     * 索引统计
     * @param client
     * @param index
     */
    public static void indexStats(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesStatsResponse response = indicesAdminClient.prepareStats(index).all().get();

        ShardStats[] shardStatsArray = response.getShards();
        for(ShardStats shardStats : shardStatsArray){
            logger.info("shardStats {}",shardStats.toString());
        }

        Map<String, IndexStats> indexStatsMap = response.getIndices();
        for(String key : indexStatsMap.keySet()){
            logger.info("indexStats {}", indexStatsMap.get(key));
        }

        CommonStats commonStats = response.getTotal();
        logger.info("total commonStats {}",commonStats.toString());

        commonStats = response.getPrimaries();
        logger.info("primaries commonStats {}", commonStats.toString());

    }


    /**
     * 创建空索引  默认setting 无mapping
     * @param client
     * @param index
     * @return
     */
    public static boolean createSimpleIndex(Client client, String index){

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
    public static boolean createIndex(Client client, String index){

        // settings
        Settings settings = Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas", 1).build();

        // mapping
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject(index)
                            .startObject("properties")
                                .startObject("query").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                                .startObject("itemList").field("type", "string").field("store", "yes").endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (Exception e) {
            logger.error("--------- createIndex 创建 mapping 失败：",e);
            return false;
        }

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index)
                .setSettings(settings)
                .addMapping(index, mappingBuilder)
                .get();

        return response.isAcknowledged();

    }

    /**
     * 删除索引
     * @param client
     * @param index
     */
    public static boolean deleteIndex(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        DeleteIndexResponse response = indicesAdminClient.prepareDelete(index).execute().actionGet();
        return response.isAcknowledged();

    }

    /**
     * 关闭索引
     * @param client
     * @param index
     * @return
     */
    public static boolean closeIndex(Client client, String index){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CloseIndexResponse response = indicesAdminClient.prepareClose(index).get();
        return response.isAcknowledged();
    }

    /**
     * 关闭索引
     * @param client
     * @param index
     * @return
     */
    public static boolean openIndex(Client client, String index){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        OpenIndexResponse response = indicesAdminClient.prepareOpen(index).get();
        return response.isAcknowledged();
    }

    /**
     * 设置映射
     * @param client
     * @param index
     * @param type
     * @return
     */
    public static boolean putIndexMapping(Client client, String index, String type){

        // mapping
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject(type)
                            .startObject("properties")
                                .startObject("name").field("type", "string").field("store", "yes").endObject()
                                .startObject("sex").field("type", "string").field("store", "yes").endObject()
                                .startObject("college").field("type", "string").field("store", "yes").endObject()
                                .startObject("age").field("type", "long").field("store", "yes").endObject()
                                .startObject("school").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (Exception e) {
            logger.error("--------- createIndex 创建 mapping 失败：", e);
            return false;
        }

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        PutMappingResponse response = indicesAdminClient.preparePutMapping(index).setType(type).setSource(mappingBuilder).get();
        return response.isAcknowledged();

    }

    /**
     * 获取mapping
     * @param client
     * @param index
     */
    public static void getIndexMapping(Client client, String index){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        GetMappingsResponse response = indicesAdminClient.prepareGetMappings(index).get();
        for(ObjectCursor<String> key : response.getMappings().keys()){
            ImmutableOpenMap<String, MappingMetaData> mapping = response.getMappings().get(key.value);
            System.out.println("------------------------");
            for(ObjectCursor<String> key2 : mapping.keys()){
                try {
                    System.out.println("-------------" + mapping.get(key2.value).sourceAsMap().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
