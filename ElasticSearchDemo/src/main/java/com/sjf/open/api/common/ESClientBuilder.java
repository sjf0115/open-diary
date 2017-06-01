package com.sjf.open.api.common;

import java.net.InetAddress;
import java.util.Map;

import com.sjf.open.utils.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESClientBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ESClientBuilder.class);

    private static Client client = null;

    private static String ip = "";
    private static String port = "";
    private static String clusterName = "";

    static {
        PropertyUtil.init("es.local.properties");
        Map<String, String> configMap = PropertyUtil.getAllFilterProps();
        ip = configMap.get("host.ip");
        port = configMap.get("host.port");
        clusterName = configMap.get("cluster.name");
    }

    /**
     * 单例模式
     * 
     * @return
     */
    public static Client builder() {
        if (client == null) {
            synchronized (ESClientBuilder.class) {
                if (client == null) {
                    client = createClient();
                }
            }
        }
        return client;
    }

    /**
     * 初始化
     *
     * @return
     */
    private static Client createClient() {
        // 设置
        Settings settings = Settings.settingsBuilder().put("client.transport.sniff", true)
                .put("cluster.name", clusterName).build();
        Client client;
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.parseInt(port)));
        } catch (Exception e) {
            logger.error("----------createClient---创建ES客户端失败", e);
            throw new RuntimeException("创建ES客户端失败 请联系开发人员");
        }
        return client;
    }

}
