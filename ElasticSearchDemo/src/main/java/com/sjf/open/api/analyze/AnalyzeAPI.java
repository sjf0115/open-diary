package com.sjf.open.api.analyze;

import java.util.List;

import com.sjf.open.api.common.ESClientBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-10-18.
 */
public class AnalyzeAPI {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeAPI.class);
    private static Client client = ESClientBuilder.builder();

    /**
     * 打印响应信息
     * @param response
     */
    private static void print(AnalyzeResponse response){

        List<AnalyzeResponse.AnalyzeToken> tokenList = response.getTokens();
        for(AnalyzeResponse.AnalyzeToken token : tokenList){
            logger.info("-------- analyzeIndex type [{}]", token.getType());
            logger.info("-------- analyzeIndex term [{}]", token.getTerm());
            logger.info("-------- analyzeIndex position [{}]", token.getPosition());
            logger.info("-------- analyzeIndex startOffSet [{}]", token.getStartOffset());
            logger.info("-------- analyzeIndex endOffSet [{}]", token.getEndOffset());
            logger.info("----------------------------------");
        }

    }

    /**
     * 某索引下某字段词条分析
     * @param index
     * @param field
     * @param value
     */
    public static void analyzeIndexAndField(String index, String field, String value){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        AnalyzeRequestBuilder analyzeRequestBuilder = indicesAdminClient.prepareAnalyze(index, value);
        analyzeRequestBuilder.setField(field);

        AnalyzeResponse response = analyzeRequestBuilder.get();

        // 打印响应信息
        print(response);

    }

    /**
     * 使用分词器进行词条分析
     * @param analyzer
     * @param value
     */
    public static void analyzeByAnalyzer(String analyzer, String value){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        AnalyzeRequestBuilder analyzeRequestBuilder = indicesAdminClient.prepareAnalyze(value);
        analyzeRequestBuilder.setAnalyzer(analyzer);

        AnalyzeResponse response = analyzeRequestBuilder.get();

        // 打印响应信息
        print(response);

    }

    public static void main(String[] args) {
        String value = "[ the, 2, quick, brown, foxes, jumped, over, the, lazy, dog's, bone ]";
        String analyzer = "standard";
        analyzeByAnalyzer(analyzer, value);
    }

}
