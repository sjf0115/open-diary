package com.sjf.open.api.update;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sjf.open.utils.ConstantUtil;

/**
 * Created by xiaosi on 16-10-13.
 *
 * 更新文档API
 *
 */
public class UpdateAPI {

    private static final Logger logger = LoggerFactory.getLogger(UpdateAPI.class);

    /**
     * 修改指定文档
     * @param client
     */
    public static void updateByDoc(Client client){

        String index = "football-index";
        String type = "football-type";
        String id = "3";

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "德布劳内");
        map.put("club", "曼城俱乐部");

        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate();
        updateRequestBuilder.setIndex(index);
        updateRequestBuilder.setType(type);
        updateRequestBuilder.setId(id);

        // Doc
        updateRequestBuilder.setDoc(map);
        // 更新
        UpdateResponse response = updateRequestBuilder.get();

        logger.info("--------- updateByScripted getIndex {}", response.getIndex());
        logger.info("--------- updateByScripted getType {}", response.getType());
        logger.info("--------- updateByScripted getId {}", response.getId());
        logger.info("--------- updateByScripted getVersion {}", response.getVersion());
        logger.info("--------- updateByScripted isCreated {}", response.isCreated());

    }


    /**
     * 使用脚本更新文档
     * @param client
     */
    public static void updateByScripted(Client client){

        String index = "football-index";
        String type = "football-type";
        String id = "3";

        // 脚本
        Script nameScript = new Script("ctx._source.name = \"德布劳内\"", ScriptService.ScriptType.INLINE, null, null);
        Script clubScript = new Script("ctx._source.club = \"曼城俱乐部\"", ScriptService.ScriptType.INLINE, null, null);
        Script removeScript = new Script("ctx._source.remove(\"country\")", ScriptService.ScriptType.INLINE, null, null);

        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate();
        updateRequestBuilder.setIndex(index);
        updateRequestBuilder.setType(type);
        updateRequestBuilder.setId(id);

        // 脚本
        updateRequestBuilder.setScript(nameScript);
        updateRequestBuilder.setScript(clubScript);
        updateRequestBuilder.setScript(removeScript);
        // 更新文档
        UpdateResponse response = updateRequestBuilder.get();

        logger.info("--------- updateByScripted getIndex {}", response.getIndex());
        logger.info("--------- updateByScripted getType {}", response.getType());
        logger.info("--------- updateByScripted getId {}", response.getId());
        logger.info("--------- updateByScripted getVersion {}", response.getVersion());
        logger.info("--------- updateByScripted isCreated {}", response.isCreated());
    }

    public static void update(){
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
        // Sets the doc to use for updates when a script is not specified.

        // Sets the doc source of the update request to be used when the document does not exists.
        //updateRequestBuilder.setUpsert(map);
        //updateRequestBuilder.setDoc(mapping);
    }
}
