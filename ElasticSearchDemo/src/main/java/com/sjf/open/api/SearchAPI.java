package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 */
public class SearchAPI {

    private static final Logger logger = LoggerFactory.getLogger(SearchAPI.class);

    private static String INDEX = "qunar-index";
    private static String BEHAVIOR_INDEX = "identitifier_count20160702";
    private static String TYPE = "employee";
    private static String TEST_INDEX = "test-index";
    private static String STU_TYPE = "stu";

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
     * 查询全部
     * 
     * @param client
     */
    public static void searchAll(Client client, String index, String type) {
        // 搜索
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        // 结果
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        logger.info("----------searchAll");
        for (SearchHit searchHit : searchHits) {
            logger.info("----------hit source: id {} source {}", searchHit.getId(), searchHit.getSource());
        } // for
    }

    /**
     * search之MultiSearch
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void multiSearch(Client client, String index, String type) {

        // 第一个搜索
        SearchRequestBuilder searchRequestBuilderOne = client.prepareSearch();
        searchRequestBuilderOne.setIndices(index);
        searchRequestBuilderOne.setQuery(QueryBuilders.termQuery("college", "计算机学院"));

        // 第二个搜索
        SearchRequestBuilder searchRequestBuilderTwo = client.prepareSearch();
        searchRequestBuilderTwo.setIndices(index);
        searchRequestBuilderTwo.setQuery(QueryBuilders.termQuery("college", "通信学院"));

        // 多搜索
        MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();
        multiSearchRequestBuilder.add(searchRequestBuilderOne);
        multiSearchRequestBuilder.add(searchRequestBuilderTwo);

        // 执行
        MultiSearchResponse multiSearchResponse = multiSearchRequestBuilder.execute().actionGet();

        // 结果
        MultiSearchResponse.Item[] responseItem = multiSearchResponse.getResponses();
        logger.info("----------multiSearch");
        for (MultiSearchResponse.Item item : responseItem) {
            SearchResponse response = item.getResponse();
            logger.info("----------multiSearch---Item");
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                logger.info("---------- hit source {}", searchHit.getSource());
            } // for
        } // for
    }

    /**
     * 分页查询
     * 
     * @param client
     * @param index
     * @param type
     * @param pageIndex
     * @param pageSize
     */
    public static void searchByPage(Client client, String index, String type, int pageIndex, int pageSize) {
        // 搜索
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        // 设置起始页
        searchRequestBuilder.setFrom(pageIndex);
        // 设置每页个数
        searchRequestBuilder.setSize(pageSize);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        logger.info("----------searchByPage");
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            logger.info("----------hit source {}", searchHit.getSource());
        } // for
    }

    /**
     * 使用scroll进行查询
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void searchByScroll(Client client, String index, String type) {
        // 搜索条件
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        searchRequestBuilder.setIndices(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setScroll(new TimeValue(60000));
        searchRequestBuilder.setSize(2);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        logger.info("--------- scroll_id {}", searchResponse.getScrollId());

        while (true) {
            logger.info("---------- scroll");
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                String source = searchHit.getSource().toString();
                logger.info("--------- source {}", source);
            } // for
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId()).setScroll(new TimeValue(60000))
                    .execute().actionGet();
            if (searchResponse.getHits().getHits().length == 0) {
                break;
            } // if
        } // while
    }

    public static void main(String[] args) {
        Client client = Common.createClient();
//        Client client = Common.createClient("user_behavior","l-innovation2.wap.cn2.qunar.com",9300);
        // searchByScroll(client, TEST_INDEX, STU_TYPE);
        // searchAll(client, TEST_INDEX, STU_TYPE);
         searchByPage(client, INDEX, TYPE, 2, 3);
         //multiSearch(client,"test-index","stu");
        client.close();
    }
}
