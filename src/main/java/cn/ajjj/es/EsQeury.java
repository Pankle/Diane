package cn.ajjj.es;

import cn.ajjj.redis.RedisSimpleUtil;
import cn.ajjj.util.DateTime;
import com.sun.deploy.util.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.mortbay.util.StringUtil;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class EsQeury {

    private  PollingBulkProcessor pollingBulkProcessor;


    //第一次查询的脱敏数据
    public static List query(JedisCluster jedisCluster,RestHighLevelClient client,String indexname, String type, String field, String filedtype) throws ParseException {
        List result = new ArrayList();

        Map<String, Object> sourceAsMap = new HashMap<>();
        try {
            if (ESUtil.indexExists(client, indexname)){
                SearchRequest searchRequest = new SearchRequest(indexname);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().
                        must(matchQuery(filedtype, field));
                searchSourceBuilder.query(boolQueryBuilder);
                searchRequest.source(searchSourceBuilder);
                SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);//同步执行
                System.out.println("!!"+searchResponse);
                SearchHits hits = searchResponse.getHits();
                if (!hits.equals("") && hits !=null) {
                    for (int i = 0; i < hits.getHits().length; i++) {
                        sourceAsMap = hits.getHits()[i].getSourceAsMap();
                        result.add(sourceAsMap);
                        System.out.println(sourceAsMap.get("event_id") );
                        jedisCluster.set(field,sourceAsMap.get("event_id").toString());
                        jedisCluster.expire(field,3000);
                    }
                }
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    //第二次查询的原始数据
    public static List query_data(JedisCluster jedisCluster,RestHighLevelClient client,String indexname, String type, String field) throws ParseException {
        List result = new ArrayList();

        Map<String, Object> sourceAsMap = new HashMap<>();
        String event_id = jedisCluster.get(field);
        if (event_id!=null&& !event_id.equals("")){
            try {
                if (ESUtil.indexExists(client, indexname)) {
                    SearchRequest searchRequest = new SearchRequest(indexname);
                    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().
                            must(matchQuery("event_id", event_id));
                    searchSourceBuilder.query(boolQueryBuilder);
                    searchRequest.source(searchSourceBuilder);
                    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);//同步执行
                    System.out.println("!!" + searchResponse);
                    SearchHits hits = searchResponse.getHits();
                    if (!hits.equals("") && hits != null) {
                        for (int i = 0; i < hits.getHits().length; i++) {
                            sourceAsMap = hits.getHits()[i].getSourceAsMap();
                            result.add(sourceAsMap);
                        }
                    }
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return result;

    }

}
