package com.qidian.mall;

import com.central.base.exception.BusinessException;
import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import com.central.common.redis.config1.RedisUtil;
import com.central.common.redis.config2.RedisRepository;
import com.central.common.redis.lock.RedisDistributedLock;
import com.qidian.mall.message.api.AliyunSmsApi;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.response.SendSmsVo;
import com.qidian.mall.user.UserCenterBusinessApplication;
import com.qidian.mall.user.api.SysUserApi;
import com.qidian.mall.user.quartz.QuartzJobManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.jasypt.encryption.StringEncryptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserCenterBusinessApplication.class)
public class UserCenterBusinessApplicationTests {
    @Autowired
    private StringEncryptor stringEncryptor;

    @Autowired
    private SysUserApi sysUserApi;
    @Autowired
    private AliyunSmsApi aliyunSmsApi;

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
//        String word = scrambleWorld("ABCAKYTNSAVD");
//        System.out.println(word);

//    RestResponse<CustomUserDetails>  restResponse =  sysUserApi.findByUsername("admin");
//        System.out.println(restResponse);
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        sendSmsDTO.setPhoneNumbers("17661977890");
        sendSmsDTO.setTemplateParam("{\\\"code\\\":\\\"123456\\\"}");
        sendSmsDTO.setBusinessType(1);
        RestResponse<SendSmsVo> restResponse = aliyunSmsApi.sendSms(sendSmsDTO);
        if(ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE.equals(restResponse.getHeader().getMessage())){
            throw new BusinessException(ConstantUtil.ERROR,ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE);
        }
        log.info("invoke alibaba send sms api,result:{} ",restResponse);
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


    @Autowired
    private com.central.common.redis.config1.RedisUtil RedisUtil;

    @Test
    public void testRedis() throws InterruptedException {
        int count =3;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i <count ; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
                task();
            }).start();
        }
        countDownLatch.await();

    }

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Autowired
    private RedissonClient redissonClient;

    private void task(){
        RLock rLock = redissonClient.getLock("lock_key"+System.currentTimeMillis());
        try {

            rLock.lock(20, TimeUnit.SECONDS);
            if(rLock.isLocked()){
                int count =(Integer) RedisUtil.get("stock");
                String result ="";
                if(count>0){
                    count--;
                    result ="扣减库存，剩余库存数量："+count;
                    RedisUtil.set("stock",count);
                    log.info(Thread.currentThread().getName()+"======>result:{}",result);
                }else {
                    result="库存不足";
                    log.info(Thread.currentThread().getName()+"======>result:{}",result);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rLock.unlock();
        }


    }
    @Test
    public void test2(){
        System.out.println(RedisUtil.get("stock"));
    }



}
