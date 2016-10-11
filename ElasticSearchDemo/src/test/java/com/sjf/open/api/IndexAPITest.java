package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-10-10.
 */
public class IndexAPITest {

    private static final Logger logger = LoggerFactory.getLogger(IndexAPITest.class);

    private Client client = Common.createClient();

    @Test
    public void isIndexExists() throws Exception {

        String index = "qunar-index";
        boolean result = IndexAPI.isIndexExists(client, index);
        logger.info("-------- isIndexExists {}", result);

    }

    @Test
    public void isTypeExists() throws Exception {

        String index = "test-index";
        String type = "stu";
        boolean result = IndexAPI.isTypeExists(client, index, type);
        logger.info("-------- isIndexExists {}", result);

    }

    @Test
    public void indexStats() throws Exception {
        String index = "test-index";
        IndexAPI.indexStats(client, index);
    }

    @Test
    public void createSimpleIndex() throws Exception {
        String index = "simple-index";
        if(IndexAPI.isIndexExists(client, index)){
            logger.info("--------- createSimpleIndex 索引 [{}] 已经存在", index);
            return;
        }

        boolean result = IndexAPI.createSimpleIndex(client, index);
        logger.info("--------- createSimpleIndex {}",result);
    }

    @Test
    public void createIndex() throws Exception {
        String index = "suggestion-index";
        if(IndexAPI.isIndexExists(client, index)){
            logger.info("--------- createIndex 索引 [{}] 已经存在", index);
            return;
        }

        boolean result = IndexAPI.createIndex(client, index);
        logger.info("--------- createIndex {}",result);
    }

    @Test
    public void deleteIndex() throws Exception {
        String index = "simple-index";
        if(!IndexAPI.isIndexExists(client, index)){
            logger.info("--------- deleteIndex 索引 [{}] 不存在", index);
            return;
        }

        boolean result = IndexAPI.deleteIndex(client, index);
        logger.info("--------- deleteIndex {}",result);
    }

    @Test
    public void closeIndex() throws Exception {
        String index = "suggestion-index";
        if(!IndexAPI.isIndexExists(client, index)){
            logger.info("--------- closeIndex 索引 [{}] 不存在", index);
            return;
        }

        boolean result = IndexAPI.closeIndex(client, index);
        logger.info("--------- closeIndex {}",result);
    }

    @Test
    public void openIndex() throws Exception {
        String index = "suggestion-index";
        if(!IndexAPI.isIndexExists(client, index)){
            logger.info("--------- closeIndex 索引 [{}] 不存在", index);
            return;
        }

        boolean result = IndexAPI.openIndex(client, index);
        logger.info("--------- closeIndex {}",result);
    }

    @Test
    public void putIndexMapping() throws Exception {
        String index = "simple-index";
        String type = "simple-type";

        if(!IndexAPI.isIndexExists(client, index)){
            logger.info("--------- putIndexMapping 索引 [{}] 不存在", index);
            return;
        }

        boolean result = IndexAPI.putIndexMapping(client, index, type);
        logger.info("--------- putIndexMapping {}",result);
    }

    @Test
    public void getIndexMapping() throws Exception {
        String index = "qunar-index";
        IndexAPI.getIndexMapping(client, index);
    }

    @Test
    public void addAliasIndex() throws Exception {
        String index = "test-index";
        String aliasName = "test";
        boolean result = IndexAPI.addAliasIndex(client, index, aliasName);
        logger.info("--------- addAliasIndex {}", result);
    }

    @Test
    public void isAliasExist() throws Exception {
        String aliasName = "simp*";
        String aliasName2 = "test";
        boolean result = IndexAPI.isAliasExist(client, aliasName, aliasName2);
        logger.info("--------- isAliasExist {}", result); // true
    }

    @Test
    public void getAliasIndex() throws Exception {
        String aliasName = "simp*";
        String aliasName2 = "test";
        IndexAPI.getAliasIndex(client, aliasName, aliasName2); // simple test
    }

    @Test
    public void deleteAliasIndex() throws Exception {
        String index = "test-index";
        String aliasName = "test";

        boolean result = IndexAPI.deleteAliasIndex(client, index, aliasName);
        logger.info("--------- deleteAliasIndex {}", result); // true
    }

    @Test
    public void updateSettingsIndex() throws Exception {
        String index = "test-index";
        Settings settings = Settings.builder().put("index.number_of_replicas", 2).build();

        if(!IndexAPI.isIndexExists(client, index)){
            logger.info("--------- updateSettingsIndex 索引 [{}] 不存在", index);
            return;
        }

        boolean result = IndexAPI.updateSettingsIndex(client, index, settings);
        logger.info("--------- updateSettingsIndex {}", result); // true
    }
}