package com.sjf.open.api;

import com.sjf.open.common.Common;
import com.sjf.open.utils.ConstantUtil;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by xiaosi on 16-10-9.
 */
public class MappingAPI {

    private static final Logger logger = LoggerFactory.getLogger(MappingAPI.class);

    /**
     * 创建Mapping
     *
     * @param client
     * @throws IOException
     */
    public static void createMapping(Client client) {

        try{
            XContentBuilder mapping = jsonBuilder().prettyPrint()
                    .startObject()
                    .startObject(ConstantUtil.STU_INDEX)
                    .startObject("properties")
                    .startObject("name").field("type", "string").field("store", "yes").endObject()
                    .startObject("sex").field("type", "string").field("store", "yes").endObject()
                    .startObject("college").field("type", "string").field("store", "yes").endObject()
                    .startObject("age").field("type", "integer").field("store", "yes").endObject()
                    .startObject("school").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            PutMappingRequest mappingRequest = Requests.putMappingRequest(ConstantUtil.STU_INDEX)
                    .type(ConstantUtil.STU_INDEX).source(mapping);
            client.admin().indices().putMapping(mappingRequest).actionGet();
        }
        catch (Exception e){

        }

    }

    public static void applyMapping(Client client, String index, String type) throws Exception {

        client.admin().indices().prepareCreate(ConstantUtil.STU_INDEX).execute().actionGet();

        String source = readJsonDefn();

        if (source != null) {
            PutMappingRequestBuilder mapping = client.admin().indices().preparePutMapping(index).setType(type);
            mapping.setSource(source);

            PutMappingResponse response = mapping.execute().actionGet();
            System.out.println(response.isAcknowledged());

        } else {
            System.out.println("mapping error");
        }

    }

    public static String readJsonDefn() throws Exception {

        String path = IndexAPI.class.getResource("/student.json").getPath();

        StringBuffer bufferJSON = new StringBuffer();

        FileInputStream input = new FileInputStream(new File(path));
        DataInputStream inputStream = new DataInputStream(input);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = br.readLine()) != null) {
            bufferJSON.append(line);
        }
        br.close();
        return bufferJSON.toString();
    }

    public static void main(String[] args) {
        Client client = Common.createClient();
        IndexAPI.createIndex(client, ConstantUtil.STU_INDEX);
        createMapping(client);
    }
}
