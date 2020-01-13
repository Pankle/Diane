package cn.ajjj.es;

import org.elasticsearch.client.RestHighLevelClient;
import java.text.ParseException;
import java.util.List;


/**
 * Created by Pankle on 2019/12/23.
 */
public class PollingBulkProcessor {
    public RestHighLevelClient client;
    public void judageAndCreateIndexName(String indexName) {
        ESUtil.judgeAndCreateIndex(client, indexName);
    }

    public  List query(String indexname, String type, String field, String filedtype) throws ParseException {
        List query = EsQeury.query(client, indexname, type, field, filedtype);
        return query;
    }

    public PollingBulkProcessor() {
        client = ESUtil.initClient("10.108.142.68", 9200, "elastic", "abcd+1234");
    }


}
