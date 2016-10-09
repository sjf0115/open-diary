package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by xiaosi on 16-9-29.
 */
public class MultiSearchAPI {

    private static final Logger logger = LoggerFactory.getLogger(MultiSearchAPI.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "student";

    private static void multiSearch(){

        Client client = Common.createClient();
        // 多搜索条件  一次请求
        MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();

        // 构造搜索条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("sex", "boy"));
        //boolQueryBuilder.must(QueryBuilders.termQuery("college", "电子工程学院"));

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX);
        searchRequestBuilder.setTypes(TYPE);
        searchRequestBuilder.setSize(2000);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        multiSearchRequestBuilder.add(searchRequestBuilder);

        // 构造搜索条件
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        boolQueryBuilder2.must(QueryBuilders.termQuery("sex", "girl"));
        //boolQueryBuilder2.must(QueryBuilders.termQuery("college", "电子工程学院"));

        SearchRequestBuilder searchRequestBuilder2 = client.prepareSearch(INDEX);
        searchRequestBuilder2.setTypes(TYPE);
        searchRequestBuilder2.setSize(2000);
        searchRequestBuilder2.setQuery(boolQueryBuilder2);
        multiSearchRequestBuilder.add(searchRequestBuilder2);

        // 执行
        MultiSearchResponse multiSearchResponse = multiSearchRequestBuilder.execute().actionGet();

        // 结果
        MultiSearchResponse.Item[] responseItem = multiSearchResponse.getResponses();
        for (MultiSearchResponse.Item item : responseItem) {
            SearchResponse searchResponse = item.getResponse();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            // 一次搜索的多个结果
            for(SearchHit searchHit : searchHits){
                Map<String,Object> sourceMap = searchHit.getSource();
                logger.info(sourceMap.toString());
            }
        } // for

        client.close();
    }

    public static void main(String[] args) {
        multiSearch();
    }
}
