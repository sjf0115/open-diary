package com.sjf.open.api.common;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 */
public class ClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClientUtil.class);

    private static String CLUSTER_NAME = "qunar-cluster";
    private static String ip = "127.0.0.1";
    private static int port = 9300;

    /**
     * 初始化
     *
     * @return
     */
    public static Client createClient() {
        Client client =createClient(CLUSTER_NAME,ip,port);
        return client;
    }

    /**
     *
     * @param clusterName
     * @param ip
     * @param port
     * @return
     */
    public static Client createClient(String clusterName, String ip, int port) {
        // 设置
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        Client client = null;
        try {
            client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        } catch (Exception e) {
            logger.error("buildClient---Host异常 {}", e);
        }
        return client;
    }

    /**
     * 返回查询结果
     *
     * @param searchResponse
     */
    public static void queryResult(SearchResponse searchResponse) {
        // 结果
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        logger.info("----------termMatch size {}", searchHits.length);
        for (SearchHit searchHit : searchHits) {
            logger.info("----------hit source: id {} score {} source{}", searchHit.getId(), searchHit.getScore(), searchHit.getSource());
        } // for
    }
}
