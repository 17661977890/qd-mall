package com.central.base.util;

/**
 * 消息验证码工具类
 */
public class SmsCodeUtil {

    public static String genVerificationCode() {
        StringBuilder code = new StringBuilder();

        for(int i = 0; i < 6; ++i) {
            code.append(Math.abs(RandomUtil.getSecureRandom().nextInt()) % 10);
        }

        return code.toString();
    }
}
