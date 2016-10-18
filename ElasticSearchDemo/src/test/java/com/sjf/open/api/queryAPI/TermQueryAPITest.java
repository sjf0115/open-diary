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
        TermQueryAPI.termQuery(client);
    }

    @Test
    public void termsQuery() throws Exception {
        TermQueryAPI.termsQuery(client);
    }

    @Test
    public void rangeQuery() throws Exception {
        TermQueryAPI.rangeQuery(client);
    }

    @Test
    public void existsQuery() throws Exception {
        TermQueryAPI.existsQuery(client);
    }

    @Test
    public void prefixQuery() throws Exception {
        TermQueryAPI.prefixQuery(client);
    }

    @Test
    public void wildcardQuery() throws Exception {
        TermQueryAPI.wildcardQuery(client);
    }
}