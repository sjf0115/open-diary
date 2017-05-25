package com.sjf.open.api.index;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sjf.open.api.common.ESClientBuilder;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.admin.indices.stats.CommonStats;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.admin.indices.stats.ShardStats;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.base.Objects;
import com.google.common.collect.UnmodifiableIterator;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;

/**
 * Created by xiaosi on 16-9-27.
 *
 * 索引管理
 *
 */
public class IndexAPI {

    private static final Logger logger = LoggerFactory.getLogger(IndexAPI.class);
    private static Client client = ESClientBuilder.builder();

    /**
     * 判断索引是否存在
     * 
     * @param index
     * @return
     */
    public static boolean isIndexExists(String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesExistsResponse response = indicesAdminClient.prepareExists(index).get();
        return response.isExists();

        /*
         * 另一种方式 IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(index); IndicesExistsResponse
         * response = client.admin().indices().exists(indicesExistsRequest).actionGet();
         */
    }

    /**
     * 判断类型是否存在
     * 
     * @param index
     * @param type
     * @return
     */
    public static boolean isTypeExists(String index, String type) {

        if (!isIndexExists(index)) {
            logger.info("--------- isTypeExists 索引 [{}] 不存在", index);
            return false;
        }

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        TypesExistsResponse response = indicesAdminClient.prepareTypesExists(index).setTypes(type).get();
        return response.isExists();

    }

    /**
     * 索引统计
     * 
     * @param index
     */
    public static void indexStats(String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesStatsResponse response = indicesAdminClient.prepareStats(index).all().get();

        ShardStats[] shardStatsArray = response.getShards();
        for (ShardStats shardStats : shardStatsArray) {
            logger.info("shardStats {}", shardStats.toString());
        }

        Map<String, IndexStats> indexStatsMap = response.getIndices();
        for (String key : indexStatsMap.keySet()) {
            logger.info("indexStats {}", indexStatsMap.get(key));
        }

        CommonStats commonStats = response.getTotal();
        logger.info("total commonStats {}", commonStats.toString());

        commonStats = response.getPrimaries();
        logger.info("primaries commonStats {}", commonStats.toString());

    }

    /**
     * 创建空索引 默认setting 无mapping
     * 
     * @param index
     * @return
     */
    public static boolean createSimpleIndex(String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index).get();
        return response.isAcknowledged();

    }

    /**
     * 创建索引 指定setting mapping
     * 
     * @param index
     * @param type
     * @param settings
     * @param mappingBuilder
     * @return
     */
    public static boolean createIndex(String index, String type, Settings settings,
            XContentBuilder mappingBuilder) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(index);

        if (!Objects.equal(settings, null)) {
            createIndexRequestBuilder.setSettings(settings);
        }

        if (!Objects.equal(mappingBuilder, null)) {
            createIndexRequestBuilder.addMapping(type, mappingBuilder);
        }

        CreateIndexResponse response = createIndexRequestBuilder.get();
        return response.isAcknowledged();

    }

    /**
     * 创建索引 指定 mapping
     * @param index
     * @param type
     * @param mappingBuilder
     * @return
     */
    public static boolean createIndex(String index, String type, XContentBuilder mappingBuilder) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(index);

        if (!Objects.equal(mappingBuilder, null)) {
            createIndexRequestBuilder.addMapping(type, mappingBuilder);
        }

        CreateIndexResponse response = createIndexRequestBuilder.get();
        return response.isAcknowledged();

    }

    /**
     * 删除索引
     * 
     * @param client
     * @param index
     */
    public static boolean deleteIndex(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        DeleteIndexResponse response = indicesAdminClient.prepareDelete(index).get();
        return response.isAcknowledged();

    }

    /**
     * 关闭索引
     * 
     * @param client
     * @param index
     * @return
     */
    public static boolean closeIndex(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CloseIndexResponse response = indicesAdminClient.prepareClose(index).get();
        return response.isAcknowledged();
    }

    /**
     * 关闭索引
     * 
     * @param client
     * @param index
     * @return
     */
    public static boolean openIndex(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        OpenIndexResponse response = indicesAdminClient.prepareOpen(index).get();
        return response.isAcknowledged();
    }

    /**
     * 判断别名是否存在
     * 
     * @param client
     * @param aliases
     * @return
     */
    public static boolean isAliasExist(Client client, String... aliases) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        AliasesExistResponse response = indicesAdminClient.prepareAliasesExist(aliases).get();
        return response.isExists();

    }

    /**
     * 为索引创建别名
     * 
     * @param client
     * @param index
     * @param alias
     * @return
     */
    public static boolean addAliasIndex(Client client, String index, String alias) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesAliasesResponse response = indicesAdminClient.prepareAliases().addAlias(index, alias).get();
        return response.isAcknowledged();

    }

    /**
     * 获取别名
     * 
     * @param client
     * @param aliases
     */
    public static void getAliasIndex(Client client, String... aliases) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        GetAliasesResponse response = indicesAdminClient.prepareGetAliases(aliases).get();
        ImmutableOpenMap<String, List<AliasMetaData>> aliasesMap = response.getAliases();

        UnmodifiableIterator<String> iterator = aliasesMap.keysIt();

        while (iterator.hasNext()) {
            String key = iterator.next();
            List<AliasMetaData> aliasMetaDataList = aliasesMap.get(key);
            for (AliasMetaData aliasMetaData : aliasMetaDataList) {
                logger.info("--------- getAliasIndex {}", aliasMetaData.getAlias());
            }
        }
    }

    /**
     * 删除别名
     * 
     * @param client
     * @param index
     * @param aliases
     * @return
     */
    public static boolean deleteAliasIndex(Client client, String index, String... aliases) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesAliasesResponse response = indicesAdminClient.prepareAliases().removeAlias(index, aliases).get();
        return response.isAcknowledged();

    }

    /**
     * 设置映射
     * 
     * @param client
     * @param index
     * @param type
     * @return
     */
    public static boolean putIndexMapping(Client client, String index, String type) {

        // mapping
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(type).startObject("properties")
                    .startObject("name").field("type", "string").field("store", "yes").endObject().startObject("sex")
                    .field("type", "string").field("store", "yes").endObject().startObject("college")
                    .field("type", "string").field("store", "yes").endObject().startObject("age").field("type", "long")
                    .field("store", "yes").endObject().startObject("school").field("type", "string")
                    .field("store", "yes").field("index", "not_analyzed").endObject().endObject().endObject()
                    .endObject();
        } catch (Exception e) {
            logger.error("--------- putIndexMapping 创建 mapping 失败：", e);
            return false;
        }

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        PutMappingRequestBuilder putMappingRequestBuilder = indicesAdminClient.preparePutMapping(index);
        putMappingRequestBuilder.setType(type);
        putMappingRequestBuilder.setSource(mappingBuilder);

        // 结果
        PutMappingResponse response = putMappingRequestBuilder.get();
        return response.isAcknowledged();

    }

    /**
     * JestClient 方式创建索引设置映射
     * 
     * @param client
     * @param index
     * @param type
     */
    public static boolean createIndexByMapping(JestClient client, String index, String type) {

        try {
            // delete the index if it exists
            IndicesExists indicesExists = new IndicesExists.Builder(index).build();
            boolean indexExists = client.execute(indicesExists).isSucceeded();
            if (indexExists) {
                DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();
                client.execute(deleteIndex);
            }
            CreateIndex createIndex = new CreateIndex.Builder(index).build();
            JestResult createResult = client.execute(createIndex);
            if (!createResult.isSucceeded()) {
                logger.error("--------- createIndexByMapping createIndex 创建索引失败");
                return false;
            }

            StringBuilder mapping = new StringBuilder("{\"");
            mapping.append(type).append("\":{").append("\"properties\":{").append("\"name\":")
                    .append("{\"type\":\"string\",\"index\":\"not_analyzed\"},").append("\"club\":")
                    .append("{\"type\":\"string\",\"index\":\"not_analyzed\"},").append("\"country\":")
                    .append("{\"type\":\"string\",\"index\":\"not_analyzed\"}").append("}").append("}").append("}");

            // System.out.println(mapping.toString());

            PutMapping.Builder putMapping = new PutMapping.Builder(index, type, mapping);
            JestResult execute = client.execute(putMapping.build());
            System.out.println("----------" + execute.getErrorMessage());
            return execute.isSucceeded();
        } catch (Exception e) {
            logger.error("--------- createIndexByMapping putMapping 创建索引失败", e);
            return false;
        }
    }

    public static boolean putIndexMapping2(Client client, String index, String type) {

        // mapping
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(type).startObject("properties")
                    .startObject("club").field("type", "string").field("index", "analyzed").field("analyzer", "english")
                    .endObject().endObject().endObject().endObject();
        } catch (Exception e) {
            logger.error("--------- putIndexMapping 创建 mapping 失败：", e);
            return false;
        }

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        PutMappingRequestBuilder putMappingRequestBuilder = indicesAdminClient.preparePutMapping(index);
        putMappingRequestBuilder.setType(type);
        putMappingRequestBuilder.setSource(mappingBuilder);

        // 结果
        PutMappingResponse response = putMappingRequestBuilder.get();
        return response.isAcknowledged();

    }

    /**
     * 获取mapping
     * 
     * @param client
     * @param index
     */
    public static void getIndexMapping(Client client, String index) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        GetMappingsRequestBuilder getMappingsRequestBuilder = indicesAdminClient.prepareGetMappings(index);
        GetMappingsResponse response = getMappingsRequestBuilder.get();

        // 结果
        for (ObjectCursor<String> key : response.getMappings().keys()) {
            ImmutableOpenMap<String, MappingMetaData> mapping = response.getMappings().get(key.value);
            for (ObjectCursor<String> key2 : mapping.keys()) {
                try {
                    logger.info("------------- {}", mapping.get(key2.value).sourceAsMap().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新设置
     * 
     * @param client
     * @param index
     * @param settings
     * @return
     */
    public static boolean updateSettingsIndex(Client client, String index, Settings settings) {

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        UpdateSettingsResponse response = indicesAdminClient.prepareUpdateSettings(index).setSettings(settings).get();
        return response.isAcknowledged();

    }

    public static void main(String[] args) {

    }

}
