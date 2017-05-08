package com.sjf.open.api.other;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by xiaosi on 16-10-8.
 *
 * 短语匹配
 *
 */
public class PhraseMatchAPI {


    /**
     * 基本短语匹配
     * @param client
     * @param index
     * @param type
     */
    public static void matchQuery(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("about", "sue alligator");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
    }


    /**
     * 基于距离 短语匹配
     * @param client
     * @param index
     * @param type
     */
    public static void matchQueryBySlop(Client client, String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("about", "sue alligator").analyzer("standard");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

    }
}
