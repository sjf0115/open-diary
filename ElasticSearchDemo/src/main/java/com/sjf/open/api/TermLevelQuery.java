package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 */
public class TermLevelQuery {
    private static final Logger logger = LoggerFactory.getLogger(Search.class);

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
     * query之Term Query
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void termQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "boy");

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
     * query之Terms Query
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void termsQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("age", "19", "18");

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
     * query之Range Query
     *
     * 注意：include lower value means that from is gt when false or gte when true include upper value means that to is lt
     * when false or lte when true
     *
     * @param client
     * @param index
     * @param type
     */
    public static void rangeQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").from(10).to(20).includeLower(true)
                .includeUpper(true);
                // QueryBuilder qb = rangeQuery("age").gte("10").lte("20");

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
     * query之existsQuery
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void existsQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.existsQuery("name");

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
        // matchAll(client,INDEX,TYPE);
        // match(client,INDEX,STUDENT_TYPE);
        // multiMatch(client,INDEX,TYPE);
        // termQuery(client,INDEX,STUDENT_TYPE);
        // termsQuery(client, INDEX, STUDENT_TYPE);
        // rangeQuery(client, INDEX, STUDENT_TYPE);
        existsQuery(client, INDEX, STUDENT_TYPE);
        client.close();
    }
}
