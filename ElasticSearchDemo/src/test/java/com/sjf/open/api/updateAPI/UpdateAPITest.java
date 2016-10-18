package com.sjf.open.api.updateAPI;

import com.sjf.open.common.Common;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 16-10-13.
 */
public class UpdateAPITest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateAPITest.class);

    private Client client = Common.createClient();

    @Test
    public void updateByScripted() throws Exception {
        UpdateAPI.updateByScripted(client);
    }

    @Test
    public void updateByDoc() throws Exception {
        UpdateAPI.updateByDoc(client);
    }
}