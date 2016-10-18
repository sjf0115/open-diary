package com.sjf.open.api.searchAPI;

import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xiaosi on 16-7-4.
 *
 *  Search API
 *
 */
public class SearchAPI {

    private static final Logger logger = LoggerFactory.getLogger(SearchAPI.class);

    /**
     * 使用scroll进行搜索
     * @param client
     */
    public static String searchByScroll(Client client) {

        String index = "football-index";
        String type = "football-type";

        // 搜索条件
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        searchRequestBuilder.setIndices(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setScroll(new TimeValue(30000));

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        String scrollId = searchResponse.getScrollId();
        logger.info("--------- searchByScroll scrollID {}", scrollId);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            String source = searchHit.getSource().toString();
            logger.info("--------- searchByScroll source {}", source);
        } // for
        return scrollId;

    }

    /**
     *  通过滚动ID获取文档
     * @param client
     * @param scrollId
     */
    public static void searchByScrollId(Client client, String scrollId){

        TimeValue timeValue = new TimeValue(30000);
        SearchScrollRequestBuilder searchScrollRequestBuilder;
        SearchResponse response;
        // 结果
        while (true) {

            logger.info("--------- searchByScroll scrollID {}", scrollId);

            searchScrollRequestBuilder = client.prepareSearchScroll(scrollId);
            // 重新设定滚动时间
            searchScrollRequestBuilder.setScroll(timeValue);
            // 请求
            response = searchScrollRequestBuilder.get();
            // 每次返回下一个批次结果 直到没有结果返回时停止 即hits数组空时
            if (response.getHits().getHits().length == 0) {
                break;
            } // if
            // 这一批次结果
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                String source = searchHit.getSource().toString();
                logger.info("--------- searchByScroll source {}", source);
            } // for
            // 只有最近的滚动ID才能被使用
            scrollId = response.getScrollId();

        } // while

    }


    /**
     * 清除滚动ID
     * @param client
     * @param scrollIdList
     * @return
     */
    public static boolean clearScroll(Client client, List<String> scrollIdList){

        ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
        clearScrollRequestBuilder.setScrollIds(scrollIdList);
        ClearScrollResponse response = clearScrollRequestBuilder.get();
        return response.isSucceeded();

    }

    /**
     * 清除滚动ID
     * @param client
     * @param scrollId
     * @return
     */
    public static boolean clearScroll(Client client, String scrollId){

        ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
        clearScrollRequestBuilder.addScrollId(scrollId);
        ClearScrollResponse response = clearScrollRequestBuilder.get();
        return response.isSucceeded();

    }

    /**
     * search之MultiSearch
     *
     * @param client
     */
    public static void multiSearch(Client client) {

        String index = "football-index";
        String type = "football-type";

        // 第一个搜索
        SearchRequestBuilder searchRequestBuilderOne = client.prepareSearch();
        searchRequestBuilderOne.setIndices(index);
        searchRequestBuilderOne.setTypes(type);
        searchRequestBuilderOne.setQuery(QueryBuilders.matchPhraseQuery("club", "巴萨罗那俱乐部"));

        // 第二个搜索
        SearchRequestBuilder searchRequestBuilderTwo = client.prepareSearch();
        searchRequestBuilderTwo.setIndices(index);
        searchRequestBuilderTwo.setTypes(type);
        searchRequestBuilderTwo.setQuery(QueryBuilders.matchPhraseQuery("country", "西班牙"));

        // 多搜索
        MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();
        multiSearchRequestBuilder.add(searchRequestBuilderOne);
        multiSearchRequestBuilder.add(searchRequestBuilderTwo);

        // 执行
        MultiSearchResponse multiSearchResponse = multiSearchRequestBuilder.get();

        // 多搜索 --- 多结果
        MultiSearchResponse.Item[] responseItem = multiSearchResponse.getResponses();
        for (MultiSearchResponse.Item item : responseItem) {
            SearchResponse response = item.getResponse();
            logger.info("-------- multiSearch");
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                logger.info("---------- multiSearch source {}", searchHit.getSource());
            } // for
        } // for

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

}
