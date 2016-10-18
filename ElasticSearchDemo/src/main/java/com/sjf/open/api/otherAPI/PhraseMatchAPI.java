package com.sjf.open.api.otherAPI;

import com.sjf.open.utils.ConstantUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.yaml.snakeyaml.scanner.Constant;

import static com.sjf.open.common.Common.createClient;
import static com.sjf.open.common.Common.queryResult;

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

        // 结果
        queryResult(searchResponse);

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

        // 结果
        queryResult(searchResponse);

    }

    public static void main(String[] args) {
        Client client = createClient();
        matchQueryBySlop(client, ConstantUtil.QUNAR_INDEX, ConstantUtil.EMPLOYEE_TYPE);
        client.close();
    }
}
