package com.qidian.mall.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * es 搜索商品信息实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoodInfo {

    private String img;

    private String price;

    private String title;
}
