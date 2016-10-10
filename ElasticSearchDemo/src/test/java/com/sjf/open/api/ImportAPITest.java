package com.sjf.open.api;


import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-10-10.
 */
public class ImportAPITest {

    private static final Logger logger = LoggerFactory.getLogger(ImportAPITest.class);

    private Client client = Common.createClient();

    @Test
    public void put() throws Exception {
        String index = "simple-index";
        String type = "simple-type";
        String id = "1";
        boolean result = ImportAPI.put(client, index, type, id);
        logger.info("--------- ImportAPITest put {}", result);
    }

    @Test
    public void putByBean() throws Exception {

    }

    @Test
    public void putByMap() throws Exception {

    }

    @Test
    public void putByJSON() throws Exception {
        String json = "{\"query\":\"厦门p4ath\",\"itemList\":\"[{\"itemName\":\"qwCA\",\"businessType\":\"flight\",\"hint\":\"AYtnG\",\"price\":274.0,\"itemCount\":7584,\"typeRatio\":58.0},{\"itemName\":\"Xy4S\",\"businessType\":\"train\",\"hint\":\"ARarr\",\"price\":941.0,\"itemCount\":4848,\"typeRatio\":4.0},{\"itemName\":\"ZT2B\",\"businessType\":\"ticket\",\"hint\":\"hruOd\",\"price\":41.0,\"itemCount\":8820,\"typeRatio\":29.0},{\"itemName\":\"jxax\",\"businessType\":\"vacation\",\"hint\":\"yWpQU\",\"price\":138.0,\"itemCount\":7463,\"typeRatio\":7.0},{\"itemName\":\"qiDd\",\"businessType\":\"flight\",\"hint\":\"LurFs\",\"price\":672.0,\"itemCount\":8599,\"typeRatio\":30.0},{\"itemName\":\"h70Z\",\"businessType\":\"train\",\"hint\":\"gGKHW\",\"price\":320.0,\"itemCount\":7238,\"typeRatio\":14.0},{\"itemName\":\"ecoa\",\"businessType\":\"train\",\"hint\":\"iNrcN\",\"price\":904.0,\"itemCount\":7801,\"typeRatio\":49.0},{\"itemName\":\"5bIy\",\"businessType\":\"train\",\"hint\":\"WkAuJ\",\"price\":698.0,\"itemCount\":4962,\"typeRatio\":67.0},{\"itemName\":\"wfK8\",\"businessType\":\"vacation\",\"hint\":\"BCTvm\",\"price\":474.0,\"itemCount\":7709,\"typeRatio\":76.0},{\"itemName\":\"HeHW\",\"businessType\":\"flight\",\"hint\":\"NQKvt\",\"price\":438.0,\"itemCount\":6127,\"typeRatio\":76.0},{\"itemName\":\"DBPS\",\"businessType\":\"ticket\",\"hint\":\"EodYB\",\"price\":26.0,\"itemCount\":3215,\"typeRatio\":47.0}]\"}\n";
        String index = "suggestion-index";
        String type = "suggestion-type";
        ImportAPI.putByJSON(client, index, index , "1",  json);
    }

    @Test
    public void bulkRequest() throws Exception {

    }

}