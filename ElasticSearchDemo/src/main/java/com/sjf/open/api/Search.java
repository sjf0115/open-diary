package com.sjf.open.api;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 */
public class Search {

    private static final Logger logger = LoggerFactory.getLogger(Search.class);

    /**
     * Get请求Builder
     *
     * @param client
     * @param index
     * @param type
     * @param doc
     * @return
     */
    private static GetRequestBuilder getGetRequestBuilder(Client client, String index, String type, String doc) {
        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, doc);
        return getRequestBuilder;
    }

    /**
     * 根据ID 进行查询
     *
     * @param index
     * @param type
     * @param id
     */
    public static void searchByID(Client client, String index, String type, String id) {
        // Get请求Builder
        GetRequestBuilder getRequestBuilder = getGetRequestBuilder(client, index, type, id);
        // 执行
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        // 输出
        if (!getResponse.isSourceEmpty()) {
            logger.info("searchByID --- source --- {}", getResponse.getSourceAsString());
        } else {
            logger.info("searchByID --- source --- 空");
        }
    }

    /**
     * 查询所有
     */
    public static void searchAll(Client client) {
        SearchResponse searchResponse = client.prepareSearch().execute().actionGet();
        logger.info("[matchAll on the whole cluster with all default options] --- {}", searchResponse.toString());
    }

    public static void multiSearch(Client client) {
        SearchRequestBuilder srb1 = client.prepareSearch().setQuery(QueryBuilders.queryStringQuery("football")).setSize(1);
        SearchRequestBuilder srb2 = client.prepareSearch().setQuery(QueryBuilders.matchQuery("first_name", "gao")).setSize(1);

        MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .execute().actionGet();

        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            logger.info("multiSearch---- " + response);
            nbHits += response.getHits().getTotalHits();
        }
    }
}
