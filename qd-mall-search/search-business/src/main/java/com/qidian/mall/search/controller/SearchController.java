package com.qidian.mall.search.controller;

import com.central.base.restparam.RestResponse;
import com.qidian.mall.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/search")
@RestController
@Api(tags = {"es搜索服务web层"})
public class SearchController {


    @Autowired
    private SearchService searchService;


    @ApiOperation(value = "数据存储",notes = "爬虫数据存储至es")
    @RequestMapping(value = "/addDataToEs/{keyWord}",method = RequestMethod.GET)
    public RestResponse<Boolean> addDataToEs(@PathVariable("keyWord") String keyWord){
        Boolean result = searchService.addDataToEs(keyWord);
        return new RestResponse<Boolean>().success(result);
    }

    @ApiOperation(value = "关键词搜索",notes = "关键词精准搜索")
    @RequestMapping(value = "/search/{keyWord}/{page}/{pageSize}",method = RequestMethod.GET)
    public RestResponse<List<Map<String,Object>>> search(@PathVariable("keyWord") String keyWord, @PathVariable("page")int page, @PathVariable("pageSize")int pageSize) throws IOException {
        List<Map<String,Object>> result = searchService.searchPage(keyWord,page,pageSize);
        return new RestResponse<List<Map<String,Object>>>().success(result);
    }

    @ApiOperation(value = "关键词高亮搜索",notes = "关键词高亮搜索")
    @RequestMapping(value = "/searchHighLight/{keyWord}/{page}/{pageSize}",method = RequestMethod.GET)
    public RestResponse<List<Map<String,Object>>> searchHighLight(@PathVariable("keyWord") String keyWord, @PathVariable("page")int page, @PathVariable("pageSize")int pageSize) throws IOException {
        List<Map<String,Object>> result = searchService.searchHighLight(keyWord,page,pageSize);
        return new RestResponse<List<Map<String,Object>>>().success(result);
    }


}
