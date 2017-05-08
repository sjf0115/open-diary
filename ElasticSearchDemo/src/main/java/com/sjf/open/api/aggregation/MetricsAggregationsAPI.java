package com.sjf.open.api.aggregation;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 *
 * 聚合功能
 */
public class MetricsAggregationsAPI {
    private static final Logger logger = LoggerFactory.getLogger(MetricsAggregationsAPI.class);

    private static String INDEX = "qunar-index";
    private static String TYPE = "employee";
    private static String TEST_INDEX = "test-index";
    private static String STU_TYPE = "stu";

    /**
     * 聚合统计
     * 
     * @param index
     * @param type
     */
    public static void aggs(Client client, String index, String type) {

        // 聚合条件
        AggregationBuilder collegeAggregationBuilder = AggregationBuilders.terms("by_college").field("college");
        // 子聚合条件
        AggregationBuilder sexAggregationBuilder = AggregationBuilders.terms("by_sex").field("sex");
        // 聚合条件下添加子聚合条件
        collegeAggregationBuilder.subAggregation(sexAggregationBuilder);

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.setQuery(QueryBuilders.matchQuery("college","计算机学院"));
        requestBuilder.addAggregation(collegeAggregationBuilder);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
        StringTerms collegeTerms = (StringTerms) aggMap.get("by_college");
        List<Terms.Bucket> collegeBucketList = collegeTerms.getBuckets();
        for(Terms.Bucket collegeBucket : collegeBucketList){
            logger.info("[{}] 有 {} 个学生", collegeBucket.getKey(), collegeBucket.getDocCount());
            StringTerms sexTerms = (StringTerms) collegeBucket.getAggregations().asMap().get("by_sex");
            List<Terms.Bucket> sexBucketList = sexTerms.getBuckets();
            for(Terms.Bucket sexBucket : sexBucketList){
                logger.info("--- [{}] 中 [{}] 有 {} 个", collegeBucket.getKey(), sexBucket.getKey(), sexBucket.getDocCount());
            } // while
        }//for
    }

    /**
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

        logger.info("员工中最小年龄 {} 聚合名称 {}", value, agg.getName());
    }

    /**
     * 聚合之max
     * 
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

        logger.info("员工中最大年龄 {} 聚合名称 {}", value, agg.getName());
    }

    /**
     * 聚合之sum
     * 
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

        logger.info("员工总年龄 {} 聚合名称 {}", value, agg.getName());
    }

    /**
     * 聚合之avg
     * 
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

        logger.info("员工平均年龄 {} 聚合名称 {}", value, agg.getName());
    }

    /**
     * 聚合之stats
     * 
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

        logger.info("员工最小年龄 {} 最大年龄 {} 平均年龄 {} 总年龄 {} 人数 {} 聚合名称 {}", min, max, avg, sum, count, agg.getName());
    }

    /**
     * 聚合之extendStats
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void extendStatsAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder extendStatsAggregation = AggregationBuilders.extendedStats("extendStats_age")
                .field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(extendStatsAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        ExtendedStats agg = searchResponse.getAggregations().get("extendStats_age");
        double min = agg.getMin();
        double max = agg.getMax();
        double avg = agg.getAvg();
        double sum = agg.getSum();
        long count = agg.getCount();
        double stdDeviation = agg.getStdDeviation();
        double sumOfSquares = agg.getSumOfSquares();
        double variance = agg.getVariance();

        logger.info("员工最小年龄 {} 最大年龄 {} 平均年龄 {} 总年龄 {} 人数 {} stdDeviation {} sumOfSquares {} variance {} 聚合名称 {}", min,
                max, avg, sum, count, stdDeviation, sumOfSquares, variance, agg.getName());
    }

    /**
     * 聚合之avg
     * 
     * @param client
     * @param index
     * @param type
     */
    public static void cardinalityAggregation(Client client, String index, String type) {

        // 聚合条件
        MetricsAggregationBuilder cardinalityAggregation = AggregationBuilders.cardinality("cardinality_age")
                .field("age");

        // 聚合
        SearchRequestBuilder requestBuilder = client.prepareSearch();
        requestBuilder.setIndices(index);
        requestBuilder.setTypes(type);
        requestBuilder.addAggregation(cardinalityAggregation);

        // 执行
        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        // 结果
        Cardinality agg = searchResponse.getAggregations().get("cardinality_age");
        long value = agg.getValue();

        logger.info("员工基数 {} 聚合名称 {}", value, agg.getName());
    }

    public static void main(String[] args) {
        // minAggregation(client,INDEX,TYPE);
        // maxAggregation(client,INDEX,TYPE);
        // sumAggregation(client,INDEX,TYPE);
        // avgAggregation(client,INDEX,TYPE);
        // statsAggregation(client,INDEX,TYPE);
        // extendStatsAggregation(client,INDEX,TYPE);
        // cardinalityAggregation(client, INDEX, TYPE);
    }
}
