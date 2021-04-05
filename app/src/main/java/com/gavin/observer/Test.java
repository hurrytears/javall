package com.gavin.observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Date;

public class Test {
    private static final Log LOG = LogFactory.getLog(DataSyncObserver.class);

    public static void main(String[] args) throws IOException {


        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("192.168.6.131", 9200, "http")));
        //单个索引请求
//        IndexRequest request = new IndexRequest("posts");
//        request.id("1");
//        String jsonString = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        IndexRequest source = request.source(jsonString, XContentType.JSON);

        //单个请求
//        GetRequest getRequest = new GetRequest(
//                "posts",
//                "1");
//
//        GetResponse fields = client.get(getRequest, RequestOptions.DEFAULT);
//        System.out.println(fields.getSourceAsString());


        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("school").id("1")
                .source(XContentType.JSON,"field", "fool"));
        request.add(new IndexRequest("school").id("6")
                .source(XContentType.JSON,"field", "baz"));
        request.add(new DeleteRequest("school").id("1"));
//        request.add(new UpdateRequest().id("6").index("school").doc(
//                "reason", "daily update",
//                "field", "uuuuuuuu"));
        request.add(new UpdateRequest("school","6").doc("field", new Date()));

        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure =
                        bulkItemResponse.getFailure();
            }
        }

        client.close();

    }
}
