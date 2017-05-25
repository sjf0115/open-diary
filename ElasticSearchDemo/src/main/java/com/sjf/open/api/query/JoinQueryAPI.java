package com.sjf.open.api.query;

import com.sjf.open.api.common.ESClientBuilder;
import com.sjf.open.utils.ESUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 17-5-24.
 */
public class JoinQueryAPI {

    private static final Logger logger = LoggerFactory.getLogger(JoinQueryAPI.class);
    private static Client client = ESClientBuilder.builder();

    /**
     * 嵌套查询
     * @param index
     * @param type
     * @param parentKey
     * @param key
     * @param value
     */
    public static void nestedQuery(String index, String type, String parentKey, String key, String value){

        NestedQueryBuilder queryBuilder = QueryBuilders.nestedQuery(parentKey, QueryBuilders.termQuery(key, value));
        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 输出
        ESUtil.print(searchResponse);

    }

    public static void main(String[] args) {
        String index = "nested_test";
        String type = "nested_type";

        nestedQuery(index, type, "comments", "comments.name", "Alice White");
    }

}
