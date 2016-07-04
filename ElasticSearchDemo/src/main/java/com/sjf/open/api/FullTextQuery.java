package com.sjf.open.api;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjf.open.common.Common;

/**
 * Created by xiaosi on 16-7-4.
 */
public class FullTextQuery {
    private static final Logger logger = LoggerFactory.getLogger(FullTextQuery.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "employee";
    private static String STUDENT_TYPE = "student";

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
            logger.info("----------hit source: id {} source{}", searchHit.getId(), searchHit.getSource());
        } // for
    }

    /**
     * query之Match All Query
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void matchAll(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * query之Match Query
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void match(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("school", "西安电子科技大学");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * query之multiMatch
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void multiMatch(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("football", "about", "interests");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        queryResult(searchResponse);
    }

    public static void main(String[] args) {
        Client client = Common.createClient();
         matchAll(client,INDEX,TYPE);
        // match(client,INDEX,STUDENT_TYPE);
        // multiMatch(client,INDEX,TYPE);
        client.close();
    }
}
