package com.qidian.mall.search.utils;

import com.qidian.mall.search.entity.GoodInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫工具类
 * @author bin
 * @data 2020-08-01
 */
public class JsoupUtils {

    public static void main(String[] args) throws Exception {
//        String url = "https://search.jd.com/Search?keyword=java";
//        // 获取document对象
//        Document document = Jsoup.parse(new URL(url), 30000);
//        //获取商品的 element 对象
//        Element j_goodsList = document.getElementById("J_goodsList");
//
////        System.out.println(j_goodsList.html());
//        //获取li元素
//        Elements li = j_goodsList.getElementsByTag("li");
//
//        for (Element e:li) {
//            String img = e.getElementsByTag("img").eq(0).attr("src");
//            String price = e.getElementsByClass("p-price").eq(0).text();
//            String title = e.getElementsByClass("p-name").eq(0).text();
//
//            System.out.println(img);
//            System.out.println(price);
//            System.out.println(title);
//        }


        new JsoupUtils().parseToData("java").forEach(System.out::println);
    }

    /**
     * 爬虫 封装es 测试搜索数据
     * @param keyWord
     * @return
     * @throws Exception
     */
    public List<GoodInfo> parseToData(String keyWord) throws Exception {
        String url = "https://search.jd.com/Search?keyword="+keyWord;
        // 获取document对象
        Document document = Jsoup.parse(new URL(url), 30000);
        //获取商品的 element 对象
        Element j_goodsList = document.getElementById("J_goodsList");
        //获取li元素
        Elements li = j_goodsList.getElementsByTag("li");
        List<GoodInfo> goodInfos = new ArrayList<>();
        for (Element e:li) {
            String img = e.getElementsByTag("img").eq(0).attr("src");
            String price = e.getElementsByClass("p-price").eq(0).text();
            String title = e.getElementsByClass("p-name").eq(0).text();

            GoodInfo goodInfo =new GoodInfo(img,price,title);
            goodInfos.add(goodInfo);

        }
        return goodInfos;
    }
}
