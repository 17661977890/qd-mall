package com.qidian.mall.usercenter;


import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserCenterClientApplicationTests {
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void contextLoads() {

        //加密方法
        System.out.println(stringEncryptor.encrypt("123456"));
        System.out.println(stringEncryptor.encrypt("123456"));
        //解密方法
        System.out.println(stringEncryptor.decrypt("JrHjkKCEYDvsg2KhjPTv6j1LCQSYUAU4"));
        System.out.println(stringEncryptor.decrypt("bsTEYo52zDjHqDS46oXaJqzEC/lrETSA"));
    }

}
