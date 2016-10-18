package com.sjf.open.api.searchAPI;

import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by xiaosi on 16-10-13.
 */
public class SearchAPITest {

    private static final Logger logger = LoggerFactory.getLogger(SearchAPI.class);

    private static Client client = Common.createClient();

    @Test
    public void multiSearch() throws Exception {
        SearchAPI.multiSearch(client);
    }

    @Test
    public void searchByScrollId() throws Exception {
        String scrollID = SearchAPI.searchByScroll(client);
        SearchAPI.searchByScrollId(client, scrollID);
    }

    @Test
    public void clearScroll() throws Exception {
        String scrollID = SearchAPI.searchByScroll(client);
        SearchAPI.clearScroll(client, scrollID);
        //SearchAPI.searchByScrollId(client, scrollID);
    }
}