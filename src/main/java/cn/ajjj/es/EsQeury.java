package cn.ajjj.es;

import cn.ajjj.util.DateTime;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class EsQeury {

    private  PollingBulkProcessor pollingBulkProcessor;
    //query 当天0点到目前 符合条件的数据

    public static List query(RestHighLevelClient client,String indexname, String type, String field, String filedtype) throws ParseException {
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
                            result.add(sourceAsMap.get("message"));
                            System.out.println(sourceAsMap.get("message") );
                        }
                    }
                    return result;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }


}
