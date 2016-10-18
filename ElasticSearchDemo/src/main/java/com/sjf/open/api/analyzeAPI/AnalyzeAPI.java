package com.sjf.open.api.analyzeAPI;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xiaosi on 16-10-18.
 */
public class AnalyzeAPI {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeAPI.class);

    /**
     * 某索引下某字段词条分析
     * @param client
     * @param index
     * @param field
     * @param value
     */
    public static void analyzeIndexAndField(Client client, String index, String field, String value){

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        AnalyzeRequestBuilder analyzeRequestBuilder = indicesAdminClient.prepareAnalyze(index, value);
        analyzeRequestBuilder.setField(field);

        AnalyzeResponse response = analyzeRequestBuilder.get();
        List<AnalyzeResponse.AnalyzeToken> tokenList = response.getTokens();
        for(AnalyzeResponse.AnalyzeToken token : tokenList){
            logger.info("-------- analyzeIndex type {}", token.getType());
            logger.info("-------- analyzeIndex term {}", token.getTerm());
            logger.info("-------- analyzeIndex position {}", token.getPosition());
            logger.info("-------- analyzeIndex startOffSet {}", token.getStartOffset());
            logger.info("-------- analyzeIndex endOffSet {}", token.getEndOffset());
            logger.info("----------------------------------");
        }

    }

}
