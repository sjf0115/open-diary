package com.sjf.open.api.query;

import com.sjf.open.api.common.ESClientBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-7-4.
 *
 * 构造查询
 *
 */
public class TermQueryAPI {

    private static final Logger logger = LoggerFactory.getLogger(TermQueryAPI.class);
    private static Client client = ESClientBuilder.builder();

    /**
     * 返回查询结果
     * 
     * @param searchResponse
     */
    public static void queryResult(SearchResponse searchResponse) {
        // 结果
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        logger.info("---------- TermQueryAPI queryResult size {}", searchHits.length);
        for (SearchHit searchHit : searchHits) {
            logger.info("---------- TermQueryAPI id {} score {} source {}", searchHit.getId(), searchHit.getScore(),
                    searchHit.getSource());
        } // for
    }

    /**
     * query之Match All Query
     *
     */
    public static void matchAllQuery(String index, String type) {

        // Query
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(queryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 词条查询
     * @param index
     * @param type
     * @param key
     * @param value
     */
    public static void termQuery(String index, String type, String key, String value) {

        // Query
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key, value);

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(termQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 多词条查询
     * 
     * @param index
     * @param type
     */
    public static void termsQuery(String index, String type, String key, String ... values) {

        // Query
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(key, values);

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(termsQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 范围查询
     * 
     * @param index
     * @param type
     */
    public static void rangeQuery(String index, String type) {

        // Query
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.from(19);
        rangeQueryBuilder.to(21);
        rangeQueryBuilder.includeLower(true);
        rangeQueryBuilder.includeUpper(true);

        // RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").gte(19).lte(21);

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(rangeQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        // 结果
        queryResult(searchResponse);

    }

    /**
     * 存在查询
     * 
     * @param index
     * @param type
     */
    public static void existsQuery(String index, String type) {

        // Query
        ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery("name");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(existsQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);

    }

    /**
     * 前缀查询
     * 
     * @param index
     * @param type
     */
    public static void prefixQuery(String index, String type) {

        // Query
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("country", "葡萄");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(prefixQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 通配符查询
     * 
     * @param index
     * @param type
     */
    public static void wildcardQuery(String index, String type) {

        // Query
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("country", "西*牙");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(wildcardQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 正则表达式查询
     * 
     * @param index
     * @param type
     */
    public static void regexpQuery(String index, String type) {

        // Query
        RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery("country", "(西班|葡萄)牙");

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(regexpQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 模糊查询 字符型
     * 
     * @param index
     * @param type
     */
    public static void fuzzyQuery(String index, String type) {

        // Query
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("country", "洗班牙");
        // 最大编辑距离
        fuzzyQueryBuilder.fuzziness(Fuzziness.ONE);
        // 公共前缀
        fuzzyQueryBuilder.prefixLength(0);

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(fuzzyQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

    /**
     * 模糊查询 数值型
     * 
     * @param index
     * @param type
     */
    public static void fuzzyQuery2(String index, String type) {

        // Query
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("age", "18");
        // 最大编辑距离
        fuzzyQueryBuilder.fuzziness(Fuzziness.TWO);

        // Search
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        searchRequestBuilder.setTypes(type);
        searchRequestBuilder.setQuery(fuzzyQueryBuilder);

        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();

        // 结果
        queryResult(searchResponse);
    }

}
