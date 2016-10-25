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
    public void search() throws Exception {
        SearchAPI.search(client);
    }

    @Test
    public void searchAll() throws Exception {
        String index = "football-index";
        String type = "football-type";
        SearchAPI.searchAll(client, index, type);
    }

    @Test
    public void searchByIndicesAndTypes() throws Exception {
        SearchAPI.searchByIndicesAndTypes(client);
    }

    @Test
    public void searchByPage() throws Exception {
        String index = "football-index";
        String type = "football-type";
        int from = 5;
        int size = 3;
        SearchAPI.searchByPage(client, index, type, from, size);
    }

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