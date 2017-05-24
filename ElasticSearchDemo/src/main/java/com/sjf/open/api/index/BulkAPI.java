package com.sjf.open.api.index;

import com.sjf.open.api.common.ESClientBuilder;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiaosi on 17-5-5.
 */
// 批量操作
public class BulkAPI {

    private static final Logger logger = LoggerFactory.getLogger(BulkAPI.class);
    private static Client client = ESClientBuilder.builder();

    /**
     * bulk
     * @param index
     * @param type
     */
    public static void bulkRequest(String index, String type, String ... sources){

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for(String source : sources){
            // Index
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
            indexRequestBuilder.setIndex(index);
            indexRequestBuilder.setType(type);
            indexRequestBuilder.setSource(source);
            indexRequestBuilder.setTTL(8000);

            bulkRequest.add(indexRequestBuilder);
        }

        BulkResponse bulkResponses = bulkRequest.get();
        if(bulkResponses.hasFailures()){
            logger.error("批量导入失败", bulkResponses.buildFailureMessage());
        }
        else{
            logger.info("批量导入成功");
        }
    }

    /**
     * 使用Bulk Processor
     * @param index
     * @param type
     * @param sources
     */
    public static void bulkProcessor(String index, String type, String ... sources){
        BulkProcessor.Builder builder = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            public void beforeBulk(long executionId, BulkRequest request) {
                logger.info("beforeBulk --------- executionId --- {} request --- {}", executionId, request.toString());
            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                logger.info("afterBulk ---------- executionId --- {} request --- {}", executionId, response.toString());
            }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                logger.error("执行失败 --------- executionId --- {} request --- {}", executionId, failure.getMessage());
            }
        });
        builder.setBulkActions(10000);
        builder.setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB));
        builder.setFlushInterval(TimeValue.timeValueSeconds(5));
        builder.setConcurrentRequests(1);
        builder.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3));
        BulkProcessor bulkProcessor = builder.build();

        for(String source : sources){
            // Index
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
            indexRequestBuilder.setIndex(index);
            indexRequestBuilder.setType(type);
            indexRequestBuilder.setSource(source);
            indexRequestBuilder.setTTL(8000);

            bulkProcessor.add(indexRequestBuilder.request());
        }

        bulkProcessor.flush();
        bulkProcessor.close();

    }

    public static void main(String[] args) {
        String player1 = "{\"country\":\"西班牙\",\"club\":\"切尔西俱乐部\",\"name\":\"佩德罗\"}";
        String player2 = "{\"country\":\"西班牙\",\"club\":\"曼城俱乐部\",\"name\":\"大卫.席尔瓦\"}";
        String player3 = "{\"country\":\"西班牙\",\"club\":\"切尔西俱乐部\",\"name\":\"法布雷加斯\"}";
        String player4 = "{\"country\":\"法国\",\"club\":\"曼联俱乐部\",\"name\":\"博格巴\"}";
        bulkProcessor("football-index1", "football-type", player1, player2, player3, player4);
    }

}
