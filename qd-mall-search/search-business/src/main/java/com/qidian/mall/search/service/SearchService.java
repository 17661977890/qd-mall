package com.qidian.mall.search.service;

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



}
