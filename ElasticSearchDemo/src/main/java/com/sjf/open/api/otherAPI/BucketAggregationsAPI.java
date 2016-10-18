package com.sjf.open.api.otherAPI;

import com.sjf.open.common.Common;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 */
public class BucketAggregationsAPI {
    private static final Logger logger = LoggerFactory.getLogger(BucketAggregationsAPI.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "employee";

    public static void globalAggregation(Client client, String index, String type) {

        // 聚合条件
        AggregationBuilder globalAggregation = AggregationBuilders.global("global_agg").subAggregation(AggregationBuilders.terms("firstNameTerms").field("first_name"));

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(globalAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Global agg = searchResponse.getAggregations().get("global_agg");
        double value = agg.getDocCount();

        logger.info("员工中global {} 聚合名称 {}",value,agg.getName());
    }

    public static void main(String[] args) {
        Client client = Common.createClient();
        globalAggregation(client,INDEX,TYPE);
        client.close();
    }

}
