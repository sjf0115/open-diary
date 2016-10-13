package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-10-11.
 */
public class DocumentAPITest {

    private static final Logger logger = LoggerFactory.getLogger(DocumentAPITest.class);

    private Client client = Common.createClient();

    @Test
    public void getByDefault() throws Exception {
        String index = "qunar-index";
        String type = "student";
        String id = "1";

        if(!IndexAPI.isIndexExists(client, index)){
            logger.info("--------- get 索引 [{}] 不存在", index);
            return;
        }

        if(!IndexAPI.isTypeExists(client, index, type)){
            logger.info("--------- get 类型 [{}] 不存在", type);
            return;
        }

        DocumentAPI.getByDefault(client, index, type, id);
    }

    @Test
    public void get() throws Exception {
        String index = "qunar-index";
        String type = "student";
        String id = "1";

        DocumentAPI.get(client, index, type, id);
    }

    @Test
    public void delete() throws Exception {
        String index = "football-index";
        String type = "football-type";
        String id = "1";

        boolean result = DocumentAPI.delete(client, index, type, id);
        logger.info("--------- delete {}", result);
    }

    @Test
    public void update() throws Exception {
        String index = "qunar-index";
        String type = "student";
        String id = "6";

        DocumentAPI.update(client, index, type, id);
    }

    @Test
    public void updateByScripted() throws Exception {
        String index = "qunar-index";
        String type = "student";
        String id = "6";

        DocumentAPI.updateByScripted(client, index, type, id);
    }
}