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
    private static String TEST_INDEX = "test-index";
    private static String TYPE = "employee";
    private static String STUDENT_TYPE = "student";
    private static String STU_TYPE = "stu";

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

    /**
     * query之Match All Query
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void matchAllQuery(Client client, String index, String type) {

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
    public static void matchQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("college", "计算机学院");

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
    public static void multiMatchQuery(Client client, String index, String type) {

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

    /**
     * query之stringQuery
     * @param client
     * @param index
     * @param type
     */
    public static void stringQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("+西安电子科技大学 -计算机学院");

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
//         matchAllQuery(client,INDEX,TYPE);
         matchQuery(client,TEST_INDEX,STU_TYPE);
//         multiMatchQuery(client,INDEX,TYPE);
//         stringQuery(client,INDEX,STUDENT_TYPE);
        client.close();
    }
}
