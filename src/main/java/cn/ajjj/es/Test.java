package cn.ajjj.es;


import java.text.ParseException;
import java.util.List;

/**
 * Created by Pankle on 2019/12/23.
 */
public class Test {
    public static PollingBulkProcessor pollingBulkProcessor ;
    public static void main(String[] args) throws ParseException {
        pollingBulkProcessor = new PollingBulkProcessor();

        pollingBulkProcessor.judageAndCreateIndexName( "desensitization");
        /*List query = pollingBulkProcessor.query("split", "doc", "o_d200irZVnqIGWncU35cKbUABCD", "userid");
        System.out.println(query.size());*/
    }
}
