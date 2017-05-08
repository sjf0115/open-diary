package com.sjf.open.api.other;

import java.util.Iterator;
import java.util.Map;

import com.sjf.open.api.common.ESClientBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.get.GetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;


/**
 * Created by xiaosi on 16-10-11.
 *
 * Document API
 *
 */
public class DocumentAPI {

    private static final Logger logger = LoggerFactory.getLogger(DocumentAPI.class);
    private static Client client = ESClientBuilder.builder();

    /**
     * 输出Get返回信息
     * @param response
     */
    private static void print(GetResponse response){

        if(Objects.equal(response, null)){
            return;
        }

        if(!response.isExists()){
            logger.info("--------- print 文档不存在");
            return;
        }

        logger.info("--------- print getIndex {}", response.getIndex());
        logger.info("--------- print getType {}", response.getType());
        logger.info("--------- print getId {}", response.getId());
        logger.info("--------- print getVersion {}", response.getVersion());


        Map<String, GetField> fieldMap = response.getFields();
        for(String key : fieldMap.keySet()){
            GetField getField = fieldMap.get(key);
            Iterator<Object> iterator = getField.iterator();
            while(iterator.hasNext()){
                Object object = iterator.next();
                logger.info("---------- print getFields key {} {}", key, object.toString());
            }
        }

        if(!response.isSourceEmpty()){
            Map<String, Object> sourceMap = response.getSource();
            logger.info("---------- print getSource {}", sourceMap.toString());
            logger.info("--------- print getSourceAsString {}", response.getSourceAsString());
        }
        else{
            logger.info("---------- print getSource  source is empty");
        }

    }

    /**
     * 请求文档
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void getByDefault(Client client, String index, String type, String id){

        GetResponse response = client.prepareGet(index, type, id).get();
        print(response);

    }

    /**
     * 多参数请求文档
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void get(Client client, String index, String type, String id){

        String[] includes = {"name", "college"};
        String[] excludes = {"name", "college"};

        // 构造请求
        GetRequestBuilder requestBuilder = client.prepareGet();
        requestBuilder.setIndex(index);
        requestBuilder.setType(type);
        requestBuilder.setId(id);
        requestBuilder.setFields("_source","name", "college", "age");
        // 只保留指定字段
        requestBuilder.setFetchSource(includes, null);
        // 排除指定字段
        // requestBuilder.setFetchSource(null, excludes);
        // 禁用/开启_source
        requestBuilder.setFetchSource(true);

        // 请求
        GetResponse response = requestBuilder.get();
        // 输出结果
        print(response);

    }

    /**
     * 删除指定文档
     * @param index
     * @param type
     * @param id
     * @return
     */
    public static boolean delete(String index, String type, String id){

        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete();
        deleteRequestBuilder.setIndex(index);
        deleteRequestBuilder.setType(type);
        deleteRequestBuilder.setId(id);

        DeleteResponse response = deleteRequestBuilder.get();
        return response.isFound();

    }

    public static void main(String[] args) {
        boolean result = delete("football-index", "football-type", "3");
        System.out.println(result);
    }

}
