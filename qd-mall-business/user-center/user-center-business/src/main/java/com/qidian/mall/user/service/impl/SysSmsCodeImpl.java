package com.qidian.mall.user.service.impl;

import com.qidian.mall.message.api.AliyunSmsApi;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import com.qidian.mall.user.response.SysSmsCodeVo;
import com.qidian.mall.user.service.ISysSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 短信发送相关业务实现
 * @Author binsun
 * @Date 2020-09-10
 * @Description
 */
@Slf4j
@Service
public class SysSmsCodeImpl implements ISysSmsService {

    @Resource
    private AliyunSmsApi aliyunSmsApi;


    @Transactional
    @Override
    public SysSmsCodeVo sendSmsVerificationCode(SysSmsCodeDTO sysSmsCodeDTO) {
        /**
         * ======================== 规则校验 ===========================
         */
        // 一天，同一个手机号最多只能发送 5 次


        // 注册
            // 一天 限制 同一个ip 只能注册5个，即同一ip 只能发送5次注册短信


        // 登录
            // 只有系统已存在的用户，才可以使用登录短信服务
            // 短信登录次数 也可以做限制



        // 重置密码
            // 重置密码 次数做限制，同一天同一个用户重置密码次数不超过 5次

        // 查该终端接收号码 最近一次发送未使用的验证码，如果发送时间不超过1分钟的，禁止再次发送，提示频繁发送

        /**
         * =========================== 数据填充，短信发送 =======================
         */

        // 生成随机验证码，业务存储验证码相关数据在前（避免分布式事务问题）

        // 调用短信发送服务，并封装返回结果


        return null;
    }
}
