package com.sjf.open.api.analyzeAPI;

import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by xiaosi on 16-10-18.
 */
public class AnalyzeAPITest {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeAPITest.class);

    private Client client = Common.createClient();

    @Test
    public void analyzeIndex() throws Exception {
        String index = "football-index";
        String field = "country";
        String value = "皇家马德里俱乐部";
        AnalyzeAPI.analyzeIndexAndField(client, index, field, value);
    }

    @Test
    public void analyzeIndex2() throws Exception {
        String index = "football-index";
        String field = "club";
        String value = "皇家马德里俱乐部";
        AnalyzeAPI.analyzeIndexAndField(client, index, field, value);
    }

    @Test
    public void analyzeByAnalyzer() throws Exception {
        // String simpleAnalyzer = "simple";
        String standardAnalyzer = "standard";
        // String whitespaceAnalyzer = "whitespace";
        String englishAnalyzer = "english";
        String value = "Set the shape to semi-transparent by calling set_trans(5)";
        AnalyzeAPI.analyzeByAnalyzer(client, standardAnalyzer, "皇家马德里");
    }

}