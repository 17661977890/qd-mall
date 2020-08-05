package com.qidian.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.qidian.mall.search.entity.GoodInfo;
import com.qidian.mall.search.service.SearchService;
import com.qidian.mall.search.utils.JsoupUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 搜索业务实现类
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * es 数据初始化  (js_goods 索引库 提前在es-head中创建好了，或者调用api创建也可以)
     * @param keyWord
     * @return
     */
    @Override
    public Boolean addDataToEs(String keyWord) {
        BulkRequest bulkRequest =new BulkRequest();
        bulkRequest.timeout("10s");
        List<GoodInfo> goodInfoList = new ArrayList<>();
        BulkResponse bulk=null;
        try {
            // 1、获取爬虫解析数据
            goodInfoList = new JsoupUtils().parseToData(keyWord);
            //2、批量添加 到es
            for (int i = 0; i <goodInfoList.size() ; i++) {
                // 不写id会随机生成
                bulkRequest.add(new IndexRequest("jd-goods").source(JSON.toJSONString(goodInfoList.get(i)), XContentType.JSON));
            }
            bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert bulk != null;
        return !bulk.hasFailures();
    }

    /**
     * 关键字搜索
     * @param keyWord
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> searchPage(String keyWord, int page, int pageSize) throws IOException {
        if(page<=1){
            page=1;
        }
        // 搜索条件封装
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title",keyWord);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 分页
        searchSourceBuilder.from(page);
        searchSourceBuilder.size(pageSize);
        // 执行搜索请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

        // 解析结果
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit h:response.getHits().getHits()) {
            list.add(h.getSourceAsMap());
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> searchHighLight(String keyWord, int page, int pageSize) throws IOException {
        if(page<=1){
            page=1;
        }
        // 搜索条件封装
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title",keyWord);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        // 多个高亮关闭
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        // 分页
        searchSourceBuilder.from(page);
        searchSourceBuilder.size(pageSize);
        // 执行搜索请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

        // 解析结果
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit h:response.getHits().getHits()) {
            // 解析高亮的字段
            Map<String, HighlightField> highlightFields = h.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            //原结果
            Map<String,Object> sourceResult =h.getSourceAsMap();
            if(title!=null){
                Text[] fragments = title.getFragments();
                StringBuilder new_title = new StringBuilder();
                for (Text t:fragments) {
                    new_title.append(t);
                }

                sourceResult.put("title",new_title);
            }
            list.add(h.getSourceAsMap());
        }

        return list;
    }
}
