package com.sjf.open.api;

import com.sjf.open.common.Common;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiaosi on 16-7-4.
 *
 * 聚合功能
 */
public class Aggregations {
    private static final Logger logger = LoggerFactory.getLogger(Aggregations.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "employee";

    /**
     * 聚合统计
     * 
     * @param index
     * @param type
     */
    public static void aggs(Client client, String index, String type) {

        // 聚合条件
        AggregationBuilder ageAggregationBuilder = AggregationBuilders.terms("by_age").field("age");
        // 子聚合条件
        AggregationBuilder firstNameAggregationBuilder = AggregationBuilders.terms("by_firstName").field("first_name");
        // 聚合条件下添加子聚合条件
        ageAggregationBuilder.subAggregation(firstNameAggregationBuilder);

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(ageAggregationBuilder);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();

        LongTerms gradeTerms = (LongTerms) aggMap.get("by_age");

        Iterator<Terms.Bucket> ageBucketIte = gradeTerms.getBuckets().iterator();

        while (ageBucketIte.hasNext()) {
            Terms.Bucket ageBucket = ageBucketIte.next();
            logger.info("{} 年龄 有 {} 个员工 {}", ageBucket.getKey(), ageBucket.getDocCount());
            StringTerms firstNameTerms = (StringTerms) ageBucket.getAggregations().asMap().get("by_firstName");
            Iterator<Terms.Bucket> firstNameBucketIte = firstNameTerms.getBuckets().iterator();
            while (firstNameBucketIte.hasNext()) {
                Terms.Bucket firstNameBucket = firstNameBucketIte.next();
                logger.info("--- {} 年龄 姓 {} 有 {} 个员工", ageBucket.getKey(), firstNameBucket.getKey(),
                        firstNameBucket.getDocCount());
            } // while
        } // while
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_metrics_aggregations.html 最小值
     *  聚合之min
     * @param client
     * @param index
     * @param type
     */
    public static void minAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder minAggregation = AggregationBuilders.min("min_age").field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(minAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Min agg = searchResponse.getAggregations().get("min_age");
        double value = agg.getValue();

        logger.info("员工中最小年龄 {} 聚合名称 {}",value,agg.getName());
    }

    /**
     *  聚合之max
     * @param client
     * @param index
     * @param type
     */
    public static void maxAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder maxAggregation = AggregationBuilders.max("max_age").field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(maxAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Max agg = searchResponse.getAggregations().get("max_age");
        double value = agg.getValue();

        logger.info("员工中最大年龄 {} 聚合名称 {}",value,agg.getName());
    }

    /**
     *  聚合之sum
     * @param client
     * @param index
     * @param type
     */
    public static void sumAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder sumAggregation = AggregationBuilders.sum("sum_age").field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(sumAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Sum agg = searchResponse.getAggregations().get("sum_age");
        double value = agg.getValue();

        logger.info("员工总年龄 {} 聚合名称 {}",value,agg.getName());
    }

    /**
     *  聚合之avg
     * @param client
     * @param index
     * @param type
     */
    public static void avgAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder avgAggregation = AggregationBuilders.avg("avg_age").field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(avgAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Avg agg = searchResponse.getAggregations().get("avg_age");
        double value = agg.getValue();

        logger.info("员工平均年龄 {} 聚合名称 {}",value,agg.getName());
    }

    /**
     *  聚合之stats
     * @param client
     * @param index
     * @param type
     */
    public static void statsAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder statsAggregation = AggregationBuilders.stats("stats_age").field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(statsAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Stats agg = searchResponse.getAggregations().get("stats_age");
        double min = agg.getMin();
        double max = agg.getMax();
        double avg = agg.getAvg();
        double sum = agg.getSum();
        long count = agg.getCount();

        logger.info("员工最小年龄 {} 最大年龄 {} 平均年龄 {} 总年龄 {} 人数 {} 聚合名称 {}",min, max, avg, sum, count, agg.getName());
    }

    public static void main(String[] args) {
        Client client = Common.createClient();
        //minAggregation(client,INDEX,TYPE);
        //maxAggregation(client,INDEX,TYPE);
        //sumAggregation(client,INDEX,TYPE);
        //avgAggregation(client,INDEX,TYPE);
        statsAggregation(client,INDEX,TYPE);
    }
}
