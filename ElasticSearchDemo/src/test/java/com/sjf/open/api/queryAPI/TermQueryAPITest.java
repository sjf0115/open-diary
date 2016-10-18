package com.sjf.open.api.queryAPI;

import com.sjf.open.api.queryAPI.TermQueryAPI;
import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.junit.Test;

/**
 * Created by xiaosi on 16-10-16.
 */
public class TermQueryAPITest {

    private Client client = Common.createClient();

    @Test
    public void matchAllQuery() throws Exception {
        TermQueryAPI.matchAllQuery(client);
    }

    @Test
    public void termQuery() throws Exception {
        String index = "simple-index";
        String type = "simple-type";
        TermQueryAPI.termQuery(client, index, type);
    }

    @Test
    public void termsQuery() throws Exception {
        String index = "football-index";
        String type = "football-type";
        TermQueryAPI.termsQuery(client, index, type);
    }

    @Test
    public void rangeQuery() throws Exception {
        String index = "qunar-index";
        String type = "student";
        TermQueryAPI.rangeQuery(client, index, type);
    }

    @Test
    public void existsQuery() throws Exception {
        String index = "football-index";
        String type = "football-type";
        TermQueryAPI.existsQuery(client, index, type);
    }

    @Test
    public void prefixQuery() throws Exception {
        String index = "football-index";
        String type = "football-type";
        TermQueryAPI.prefixQuery(client, index, type);
    }

    @Test
    public void wildcardQuery() throws Exception {
        String index = "football-index";
        String type = "football-type";
        TermQueryAPI.wildcardQuery(client, index, type);
    }

    @Test
    public void regexpQuery() throws Exception {
        String index = "football-index";
        String type = "football-type";
        TermQueryAPI.regexpQuery(client, index, type);
    }

    @Test
    public void fuzzyQuery() throws Exception {
        String index = "football-index";
        String type = "football-type";
        TermQueryAPI.fuzzyQuery(client, index, type);
    }

    @Test
    public void fuzzyQuery2() throws Exception {
        String index = "qunar-index";
        String type = "student";
        TermQueryAPI.fuzzyQuery2(client, index, type);
    }
}