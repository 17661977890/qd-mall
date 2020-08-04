package com.qidian.mall.search.controller;

import com.qidian.mall.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/search")
@RestController
@Api(tags = {"es搜索服务web层"})
public class SearchController {


    @Autowired
    private SearchService searchService;


}
