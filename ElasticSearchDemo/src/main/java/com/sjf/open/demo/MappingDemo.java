package com.sjf.open.demo;

import com.google.common.base.Objects;
import com.sjf.open.api.index.IndexAPI;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaosi on 17-6-1.
 */
public class MappingDemo {

    private static final Logger logger = LoggerFactory.getLogger(IndexDemo.class);

    /**
     * Nested By Object Mapping
     * 
     * @param type
     * @return
     */
    public static XContentBuilder objectNestedMapping(String type) {

        // mapping
        XContentBuilder mappingBuilder = null;
        try {
            mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(type)
                    .startObject("properties")
                        .startObject("userInfo")
                            .startObject("properties")
                                .startObject("gid").field("type", "string").field("index", "not_analyzed").field("store", "yes").endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            logger.error("--------- create mapping error", e);
        }
        return mappingBuilder;

    }

    public static XContentBuilder nestedMapping(String type) {

        // mapping
        XContentBuilder mappingBuilder = null;
        try {
            mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(type).startObject("properties")
                    .startObject("userInfo").field("type", "nested").startObject("properties").startObject("gid")
                    .field("type", "string").field("index", "not_analyzed").endObject().endObject().endObject()
                    .endObject().endObject().endObject();
        } catch (Exception e) {
            logger.error("--------- create mapping error", e);
        }
        return mappingBuilder;

    }

    /**
     * Analyzer Mapping
     * 
     * @param type
     * @return
     */
    public static XContentBuilder analyzerMapping(String type) {

        // mapping
        XContentBuilder mappingBuilder = null;
        try {
            mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(type).startObject("properties")
                    .startObject("club").field("type", "string").field("index", "analyzed").field("analyzer", "english")
                    .endObject().endObject().endObject().endObject();
        } catch (Exception e) {
            logger.error("--------- create mapping error", e);
        }
        return mappingBuilder;

    }

    public static void main(String[] args) {
        String index = "my-index-test";
        String type = "my-type-test";

        XContentBuilder mapping = objectNestedMapping(type);

        if (Objects.equal(mapping, null)) {
            logger.info("---------　创建映射失败");
            return;
        }

        boolean exists = IndexAPI.isIndexExists(index);
        if (exists) {
            IndexAPI.deleteIndex(index);
        }

        boolean result = IndexAPI.createIndex(index, type, mapping);
        if (result) {
            logger.info("---------　创建索引成功");
        } else {
            logger.info("--------- 创建索引失败");
        }
    }
}
