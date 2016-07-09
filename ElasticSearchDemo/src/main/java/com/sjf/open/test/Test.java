package com.sjf.open.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sjf.open.api.MetricsAggregations;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.HppcMaps;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-6-29.
 */

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "employee";
    private static String TYPE2 = "employee";
    private static Client client;

    /**
     * 初始化
     *
     * @return
     */
    private static Client Init() {
        // 设置
        Settings settings = Settings.settingsBuilder().put("cluster.name", "qunar-cluster").build();
        Client client = null;
        try {
            client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            logger.error("buildClient---Host异常 {}", e);
        }
        return client;
    }

    /**
     * 中文分词
     * @param str
     * @return
     */
    public static String checkChinese(String str){
        String sb = new String();
        Pattern pattern = Pattern.compile("[\u3007\u4E00-\u9FCB\uE815-\uE864]");//只匹配一个中文字符
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            sb += matcher.group()+";";
        }
        return sb.toString();
    }

    public static void test(String index, String type){
        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("gid","0009F361-6E5F-E9DF-5219-6F993EC25BFE");
        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            String age = searchHit.getSource().get("gid").toString();
            Map<String,String> interest = (Map<String,String>)searchHit.getSource().get("indexCount");
            logger.info("------------------ ************** --- age {} interest {}", age,interest.toString());
        } // for
    }

    public static void main(String[] args) {

        client = Init();
        test("qunar-index","business");
        client.close();
    }
}
