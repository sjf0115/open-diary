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
public class CompoundQuery {
    private static final Logger logger = LoggerFactory.getLogger(CompoundQuery.class);

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
     * query之Bool Query
     *
     * @param client
     * @param index
     * @param type
     */
    public static void boolQuery(Client client, String index, String type) {

        // Query
        // QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("age",
        // "21")).must(QueryBuilders.termQuery("sex", "girl"));
        // QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("age",
        // "21")).mustNot(QueryBuilders.termQuery("sex", "girl"));
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("age", "21"))
                .should(QueryBuilders.termQuery("sex", "girl"));

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
     * query之indicesQuery
     * @param client
     * @param index
     * @param index2
     * @param type
     */
    public static void indicesQuery(Client client, String index, String index2, String type) {

        QueryBuilder queryBuilder = QueryBuilders.indicesQuery(QueryBuilders.termQuery("age", "18"), index, index2)
                .noMatchQuery(QueryBuilders.termQuery("sex", "boy"));

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
//        boolQuery(client, INDEX, STUDENT_TYPE);
        indicesQuery(client, INDEX, "qunar",STUDENT_TYPE);
        client.close();
    }
}
