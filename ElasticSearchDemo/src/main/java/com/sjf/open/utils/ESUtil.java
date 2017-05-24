package com.sjf.open.utils;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 17-5-24.
 */
public class ESUtil {

    private static final Logger logger = LoggerFactory.getLogger(ESUtil.class);

    /**
     * 输出搜索结果
     * 
     * @param searchResponse
     */
    public static void print(SearchResponse searchResponse) {
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            logger.info("---------- source {}", searchHit.getSource());
        } // for
    }

}
