package com.qidian.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.qidian.mall.search.entity.GoodInfo;
import com.qidian.mall.search.service.SearchService;
import com.qidian.mall.search.utils.JsoupUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
