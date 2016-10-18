package com.sjf.open.api.queryAPI;

import com.sjf.open.common.Common;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by xiaosi on 16-7-4.
 *
 * 构造查询
 *
 */
public class TermQueryAPI {

    private static final Logger logger = LoggerFactory.getLogger(TermQueryAPI.class);

    /**
     * 返回查询结果
     * 
     * @param searchResponse
     */
    public static void queryResult(SearchResponse searchResponse) {
        // 结果
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        logger.info("---------- TermQueryAPI queryResult size {}", searchHits.length);
        for (SearchHit searchHit : searchHits) {
            logger.info("---------- TermQueryAPI id {} score {} source {}", searchHit.getId(), searchHit.getScore(),
                    searchHit.getSource());
        } // for
    }

    /**
     * query之Match All Query
     *
     * @param client
     */
    public static void matchAllQuery(Client client) {

        String index = "football-index";
        String type = "football-type";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * query之Term Query
     * 
     * @param client
     */
    public static void termQuery(Client client) {

        String index = "simple-index";
        String type = "simple-type";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.termQuery("country", "AWxhOn".toLowerCase());

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * query之Terms Query
     *
     * @param client
     */
    public static void termsQuery(Client client) {

        String index = "football-index";
        String type = "football-type";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("country", "比利时", "德国");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * query之Range Query
     *
     * include lower value means that from is gt when false or gte when true
     * include upper value means that to is lt when false or lte when true
     *
     * @param client
     */
    public static void rangeQuery(Client client) {

        String index = "qunar-index";
        String type = "student";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").from(19).to(21).includeLower(true).includeUpper(true);
        //QueryBuilder queryBuilder = QueryBuilders.rangeQuery("age").gte(19).lte(21);

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
     */
    public static void existsQuery(Client client) {

        String index = "football-index";
        String type = "football-type";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.existsQuery("name");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 前缀查询
     * @param client
     */
    public static void prefixQuery(Client client) {
        String index = "football-index";
        String type = "football-type";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.prefixQuery("club", "皇家");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 通配符查询
     * @param client
     */
    public static void wildcardQuery(Client client){

        String index = "football-index";
        String type = "football-type";

        // Query
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("country", "*西班*");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * query之中文短语查询
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void termPhraseQuery(Client client, String index, String type) {

        // Query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("college", "计"));
        boolQueryBuilder.must(QueryBuilders.termQuery("college", "算"));
        boolQueryBuilder.must(QueryBuilders.termQuery("college", "机"));
        boolQueryBuilder.must(QueryBuilders.termQuery("college", "学"));
        boolQueryBuilder.must(QueryBuilders.termQuery("college", "院"));
        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(boolQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        queryResult(searchResponse);
    }

    /**
     *
     * @param client
     * @param index
     * @param type
     */
    public static void phraseQuery(Client client, String index, String type) {

        // Query
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("school", "西安");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(matchQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        queryResult(searchResponse);
    }

}
