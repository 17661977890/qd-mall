package com.qidian.mall;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.qidian.mall.quartz.QuartzJobManager;
import com.qidian.mall.quartz.TestQuartz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.jasypt.encryption.StringEncryptor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserCenterBusinessApplicationTests {
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void contextLoads() {
        //加密方法
        System.out.println(stringEncryptor.encrypt("admin"));
        System.out.println(stringEncryptor.encrypt("Iflytek@123"));
        //解密方法
        System.out.println(stringEncryptor.decrypt("LmL0YwWu7iem5J+jD9pAS5v8VZvXl63Y"));
        System.out.println(stringEncryptor.decrypt("JrHjkKCEYDvsg2KhjPTv6j1LCQSYUAU4"));
    }

//    @Test
//    public void testPdfRead(){
//        //创建PdfDocument实例
//        PdfDocument doc= new PdfDocument();
//        //加载PDF文件
//        doc.loadFromFile("/Users/minyoung/Desktop/JGJ146-2013 建设工程施工现场环境与卫生标准.pdf");
//        StringBuilder sb= new StringBuilder();
//
//        PdfPageBase page;
//        //遍历PDF页面，获取文本
//        for(int i=0;i<doc.getPages().getCount();i++){
//            page=doc.getPages().get(i);
//            sb.append(page.extractText(true));
//            if(page.extractImages()!=null){
//                for (BufferedImage image:page.extractImages()){
//                    if(image!=null){
//
//                    }
//                }
//            }
//        }
//        System.out.println(sb.toString());
//        doc.close();
//    }

    @Test
    public void testTask() throws Exception {
//        QuartzJobManager.getInstance().addJob(TestQuartz.class, "testJob","Group1", "0/5 * * * * ? ");
        QuartzJobManager.getInstance().resumeJob("testJob","Group1");
    }


    @Test
    public void test(){
        String word = scrambleWorld("ABCAKYTNSAVD");
        System.out.println(word);
    }

    private String scrambleWorld(String word){
        StringBuilder changeWords =new StringBuilder();
        int j =0;
        while (j< word.length()-1) {
            String subStr = word.substring(j, j+1);
            String next = word.substring(j+1,j+2);
            if("A".equals(subStr)){
                changeWords.append(next).append(subStr);
                j+=2;
            }else {
                changeWords.append(subStr);
                j++;
            }
        }
        if(j<word.length()){
            changeWords.append(word.substring(j,j+1));
        }
        return changeWords.toString();
    }


}
