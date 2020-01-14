package cn.ajjj.es;

import cn.ajjj.redis.RedisSimpleUtil;
import org.elasticsearch.client.RestHighLevelClient;
import redis.clients.jedis.JedisCluster;

import java.text.ParseException;
import java.util.List;


/**
 * Created by Pankle on 2019/12/23.
 */
public class PollingBulkProcessor {
    public RestHighLevelClient client;
    private static JedisCluster jedisCluster;
    static {
        jedisCluster = RedisSimpleUtil.getInstance();
    }
    public void judageAndCreateIndexName(String indexName) {
        ESUtil.judgeAndCreateIndex(client, indexName);
    }

    public  List query(String indexname, String type, String field, String filedtype,String cookid,String userid) throws ParseException {
        //保存登录的用户和cookid，又来防止横向越权操作
        jedisCluster.set(cookid.toString(), userid.toString());
        jedisCluster.expire(cookid.toString(),1800);
        List query = EsQeury.query(jedisCluster,client, indexname, type, field, filedtype);
        return query;
    }
    public  List query_data(String indexname, String type, String field, String filedtype,String cookid,String userid) throws ParseException {
        //判断用户id与上次传过来的是否一致
        //保存登录的用户和cookid，又来防止横向越权操作
        String uid=jedisCluster.get(cookid.toString());
        if (uid.equals(userid)) {
            List query = EsQeury.query_data(jedisCluster,client, indexname, type, field);
            return query;
        }else {
            return null;
        }
    }

    public PollingBulkProcessor() {
        client = ESUtil.initClient("hostname", 9200, "elastic", "abcd+1234");
    }


}
