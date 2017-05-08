package com.sjf.open.api.common;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * JestClient构建工具类(JestClient建议使用单例模式,不需要在每次请求时都创建)
 */
public class JestClientBuilder {

    private final static int SOCKE_READ_TIME_OUT = 80000;
    public static JestClient jestClient;

    public static JestClient build(String elasticsearchUrl) {
        if (jestClient == null) {
            JestClientFactory jestClientFactory = new JestClientFactory();
            jestClientFactory.setHttpClientConfig(new HttpClientConfig.Builder(elasticsearchUrl).multiThreaded(true)
                    .readTimeout(SOCKE_READ_TIME_OUT).build());
            jestClient = jestClientFactory.getObject();
        }
        return jestClient;

    }

    public static JestClient build(String elasticsearchUrl, String user, String password) {
        JestClientFactory jestClientFactory = new JestClientFactory();
        jestClientFactory.setHttpClientConfig(new HttpClientConfig.Builder(elasticsearchUrl)
                .defaultCredentials(user, password).multiThreaded(true).build());
        return jestClientFactory.getObject();
    }

}
