package com.qidian.mall.search.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 搜索业务接口
 */
public interface SearchService {

    /**
     * 向es中添加数据 （爬虫解析数据到es）
     * @param keyWord
     * @return
     */
    Boolean addDataToEs(String keyWord);

    /**
     * 关键字搜索
     * @param keyWord
     * @param page
     * @param pageSize
     * @return
     */
    List<Map<String,Object>> searchPage(String keyWord,int page,int pageSize) throws IOException;

    /**
     * 关键字 高亮 搜索
     * @param keyWord
     * @param page
     * @param pageSize
     * @return
     */
    List<Map<String,Object>> searchHighLight(String keyWord,int page,int pageSize) throws IOException;

}
