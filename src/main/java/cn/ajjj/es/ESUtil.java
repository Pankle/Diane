package cn.ajjj.es;


import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;


import java.io.IOException;

/**
 * Created by Pankle on 2019/12/23.
 */
public class ESUtil {

    public static RestHighLevelClient initClient(String esNode, int port , String userName , String password) {
        Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                new BasicHeader("Role", "Read") };
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(esNode, port)).setDefaultHeaders(headers)
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder arg0) {
                        System.out.println("认证成功");
                        return arg0.setDefaultCredentialsProvider(credentialsProvider);
                    }
                }));
        return client;
    }
    /**
     * 判断指定的索引名是否存在
     * @param index 索引名
     * @return  存在：true; 不存在：false;
     */
    public static  boolean indexExists(RestHighLevelClient client, String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        request.local(false);
        request.humanReadable(true);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    public static void judgeAndCreateIndex(RestHighLevelClient client, String indexName) {
        System.out.println("开始创建index>>>>>"+indexName);
        try {
                if (!indexExists(client, indexName)) {
                    System.out.println("现在开始创建");
                    CreateIndexRequest request = new CreateIndexRequest(indexName);
                    request.settings(Settings.builder()
                            .put("index.number_of_shards", 3)
                            .put("index.number_of_replicas", 1)

                    );
                    System.out.println("this is indexname:"+indexName);
                    client.indices().create(request,RequestOptions.DEFAULT);
                }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("###" + e.getMessage());
        }
    }

/*    public static BulkProcessor getBulkProcessor(RestHighLevelClient restClient) {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                //提交结束后调用（无论成功或失败）
                System.out.println("提交" + response.getItems().length + "个文档，用时"
                        + response.getIngestTookInMillis() + "MS" + (response.hasFailures() ? " 有文档提交失败！" : ""));

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                //提交结束且失败时调用
                System.out.println(" 有文档提交失败！after failure=" + failure);
            }
        };

        return BulkProcessor
                .builder(restClient::bulkAsync, listener)
                .setBulkActions(5000)//文档数量达到5000时提交
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))//总文档体积达到5MB时提交 //
                .setFlushInterval(TimeValue.timeValueSeconds(10))//每10S提交一次（无论文档数量、体积是否达到阈值）
//                    .setConcurrentRequests(1)//加1后为可并行的提交请求数，即设为0代表只可1个请求并行，设为1为2个并行
                .build();

    }*/


}
