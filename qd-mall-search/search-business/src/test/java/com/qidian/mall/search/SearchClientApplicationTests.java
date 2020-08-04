package com.qidian.mall.search;


import com.alibaba.fastjson.JSON;
import com.qidian.mall.search.entity.EsDocument;
import org.apache.ibatis.annotations.Update;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;

import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * es 相关api相关测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SearchClientApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 测试索引的创建 : PUT 命令
     */
    @Test
    public void testEsCreateIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest request =new CreateIndexRequest("qd-index");
        // 客户端执行请求
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }


    /**
     * 测试索引的删除 :
     */
    @Test
    public void testEsDeleteIndex() throws IOException {
        //创建索引请求
        DeleteIndexRequest request =new DeleteIndexRequest("qd-index");
        // 客户端执行请求
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete);
    }


    /**
     * 测试索引的是否存在 :
     */
    @Test
    public void testEsGetIndex() throws IOException {
        //创建索引请求
        GetIndexRequest request =new GetIndexRequest("test1");
        // 客户端执行请求
        boolean response = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 添加文档
     */
    @Test
    public void testAddDocument() throws IOException {
        // 创建对象
        EsDocument esDocument = new EsDocument("测试","A",1);
        //创建请求
        IndexRequest request = new IndexRequest("qd-index");

        // 规则 put /qd-index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        // 将我们的数据放入请求 json
        request.source(JSON.toJSONString(esDocument), XContentType.JSON);
        // 客户端发送请求，获取结果
        IndexResponse response = client.index(request,RequestOptions.DEFAULT);
        System.out.println(response.toString());
        System.out.println(response.status());


    }

    /**
     * 获取文档，判断是否存在
     */
    @Test
    public void testExistDoc() throws IOException {
        GetRequest getRequest = new GetRequest("qd-index","1");
        //不获取返回的_source 的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean result =client.exists(getRequest,RequestOptions.DEFAULT);
        System.out.println(result);
    }

    /**
     * 获取文档信息
     */
    @Test
    public void testGetDoc() throws IOException {
        GetRequest getRequest = new GetRequest("qd-index","1");
        GetResponse response = client.get(getRequest,RequestOptions.DEFAULT);

        // 打印文档内容：{"name":"测试","size":1,"type":"A"}
        System.out.println(response.getSourceAsString());
        // 返回全部内容（和get命令一样）
        //{"_index":"qd-index","_type":"_doc","_id":"1","_version":1,"_seq_no":0,"_primary_term":1,"found":true,"_source":{"name":"测试","size":1,"type":"A"}}
        System.out.println(response);
    }

    /**
     * 更新文档信息：命令如下
     * POST /test1/type1/1/_update
     * {
     *   "doc":{"name":"sunbin123"}
     * }
     */
    @Test
    public void  testUpdateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("qd-index","1");
        updateRequest.timeout("1s");
        EsDocument esDocument = new EsDocument("孙彬","B",2);

        updateRequest.doc(JSON.toJSONString(esDocument),XContentType.JSON);
        UpdateResponse response = client.update(updateRequest,RequestOptions.DEFAULT);

        System.out.println(response);

    }


    /**
     * 删除文档
     * DELETE /test2/1
     */
    @Test
    public void testDeleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("qd-index","1");
        deleteRequest.timeout("1s");
        DeleteResponse response = client.delete(deleteRequest,RequestOptions.DEFAULT);
        System.out.println(response);
    }


    /**
     * 大数据量批量请求处理（新增文档、修改、删除）
     */
    @Test
    public void testBatchAdd() throws IOException {
        BulkRequest bulkRequest =new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<EsDocument> esDocuments = new ArrayList<>();
        esDocuments.add(new EsDocument("sunbin","1",1));
        esDocuments.add(new EsDocument("sunbin","2",1));
        esDocuments.add(new EsDocument("sunbin","3",1));
        esDocuments.add(new EsDocument("sunbin","4",1));
        esDocuments.add(new EsDocument("sunbin","5",1));
        esDocuments.add(new EsDocument("sunbin","6",1));
        esDocuments.add(new EsDocument("sunbin","17",1));
        // 批处理请求（批量新增，批量修改，批量删除 就在add（）中加入不同请求就行了）
        for (int i = 0; i <esDocuments.size() ; i++) {
            // 不写id会随机生成
            bulkRequest.add(new IndexRequest("qd-index").id(""+(i+1)).source(JSON.toJSONString(esDocuments.get(i)),XContentType.JSON));
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 返回false 就代表成功
        System.out.println(bulk.hasFailures());
    }

    /**
     * 搜索相关
     * 1、搜索请求：SearchRequest
     * 2、条件构造：SearchSourceBuilder
     * 3、termQueryBuilder 精确匹配
     * 4、MatchAllQueryBuilder 模糊匹配
     * 5、HighlightBuilder 构建高亮
     *    -----------------QueryBuilders.xxx
     */
    @Test
    public void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("qd-index");
        //构建搜索条件

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // QueryBuilders ：实现各种匹配方式的构建工具
        // term 匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "sunbin");
        searchSourceBuilder.query(termQueryBuilder);

        // match 模糊匹配
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
//        searchSourceBuilder.query(matchAllQueryBuilder);
        // 使用默认值
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println("\t"+JSON.toJSONString(search.getHits()));
        System.out.println("======================");
        for (SearchHit h:search.getHits().getHits()) {
            System.out.println(h.getSourceAsMap());
        }


    }

}
