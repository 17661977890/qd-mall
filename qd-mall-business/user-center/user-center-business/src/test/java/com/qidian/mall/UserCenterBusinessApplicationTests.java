package com.qidian.mall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.jasypt.encryption.StringEncryptor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserCenterBusinessApplicationTests {
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void contextLoads() {
        //加密方法
        System.out.println(stringEncryptor.encrypt("123456"));
        System.out.println(stringEncryptor.encrypt("123456"));
        //解密方法
        System.out.println(stringEncryptor.decrypt("LmL0YwWu7iem5J+jD9pAS5v8VZvXl63Y"));
        System.out.println(stringEncryptor.decrypt("JrHjkKCEYDvsg2KhjPTv6j1LCQSYUAU4"));
    }

}
