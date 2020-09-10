package com.central.base.util;

import com.central.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 随机数工具类
 *
 * @author: dcliu
 * @date: 2018/9/17 17:10
 */
public final class RandomUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RandomUtil.class);

    /**
     * SHA1加密算法
     */
    private static final String ALGORITHM = "SHA1PRNG";

    private RandomUtil(){}

    /**
     * 获取SecureRandom随机数生成器,能抵挡加密攻击
     *
     * @return
     */
    public static SecureRandom getSecureRandom(){
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
            throw new BusinessException(ConstantUtil.ERROR,"NoSuchAlgorithmException:" + e.getMessage());
        }
        return random;
    }

}
