package com.sjf.open.api;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.sjf.open.utils.ConstantUtil;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by xiaosi on 16-10-11.
 *
 * Document API
 *
 */
public class DocumentAPI {

    private static final Logger logger = LoggerFactory.getLogger(DocumentAPI.class);

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
     * @param client
     * @param index
     * @param type
     * @param id
     * @return
     */
    public static boolean delete(Client client, String index, String type, String id){

        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete();
        deleteRequestBuilder.setIndex(index);
        deleteRequestBuilder.setType(type);
        deleteRequestBuilder.setId(id);

        DeleteResponse response = deleteRequestBuilder.get();
        return response.isFound();

    }

    /**
     * 修改指定文档
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void update(Client client, String index, String type, String id){

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "李昂");
        map.put("sex", "boy");
        map.put("age", 23);
        map.put("college", "计算机学院");
        map.put("school", "麻省理工大学");

        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder().prettyPrint()
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate();
        updateRequestBuilder.setIndex(index);
        updateRequestBuilder.setType(type);
        updateRequestBuilder.setId(id);

        // Sets the doc to use for updates when a script is not specified.
        updateRequestBuilder.setDoc(mapping);
        // Sets the doc source of the update request to be used when the document does not exists.
        updateRequestBuilder.setUpsert(map);



        UpdateResponse response = updateRequestBuilder.get();

        logger.info("-------- update index {} type {} id {}", response.getIndex(), response.getType(), response.getId());
        logger.info("-------- update isCreated {}", response.isCreated());

    }

    /**
     * 使用脚本更新文档
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void updateByScripted(Client client, String index, String type, String id){

        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate();
        updateRequestBuilder.setIndex(index);
        updateRequestBuilder.setType(type);
        updateRequestBuilder.setId(id);

        // 脚本
        Script collegeScript = new Script("ctx._source.college = \"软件学院2\"", ScriptService.ScriptType.INLINE, null, null);
        updateRequestBuilder.setScript(collegeScript);
        // 更新文档
        UpdateResponse response = updateRequestBuilder.get();

    }


    /*// 参数
    Map<String, Object> param = Maps.newConcurrentMap();
    param.put("name", "高准翼");
    param.put("college", "软件学院");
    //updateRequestBuilder.setScript(nameScript);
    //Script nameScript = new Script("ctx._source.name = name", ScriptService.ScriptType.INLINE, "native", param);*/
}
