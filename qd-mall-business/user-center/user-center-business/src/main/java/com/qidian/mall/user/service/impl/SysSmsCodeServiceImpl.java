package com.qidian.mall.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseServiceImpl;
import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import com.central.base.util.IdWorker;
import com.central.base.util.SmsCodeUtil;
import com.qidian.mall.message.api.AliyunSmsApi;
import com.qidian.mall.message.enums.SmsCodeVerifyEnum;
import com.qidian.mall.message.enums.SmsTemplateTypeEnum;
import com.qidian.mall.message.request.SendSmsDTO;
import com.qidian.mall.message.response.SendSmsVo;
import com.qidian.mall.user.entity.SysSmsCode;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.mapper.SysSmsCodeMapper;
import com.qidian.mall.user.mapper.SysUserMapper;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import com.qidian.mall.user.response.SysSmsCodeVo;
import com.qidian.mall.user.service.ISysSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 短信发送相关业务实现
 * @Author binsun
 * @Date 2020-09-10
 * @Description
 */
@Slf4j
@Service
public class SysSmsCodeServiceImpl extends BaseServiceImpl implements ISysSmsService {

    @Resource
    private AliyunSmsApi aliyunSmsApi;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysSmsCodeMapper sysSmsCodeMapper;

    private static final int SMS_COUNT = 5;
    private static final int USER_COUNT = 5;


    /**
     * 发送验证码
     * @param sysSmsCodeDTO
     * @return
     */
    @Transactional
    @Override
    public SysSmsCodeVo sendSmsVerificationCode(SysSmsCodeDTO sysSmsCodeDTO) {
        // 规则校验
        smsRuleCheck(sysSmsCodeDTO);
        SysSmsCodeVo sysSmsCodeVo = new SysSmsCodeVo();
        try {
            // 数据填充入库
            SysSmsCode sysSmsCode = genData(sysSmsCodeDTO);
            sysSmsCodeMapper.insert(sysSmsCode);
            // 调用短信发送服务，并封装返回结果
            SendSmsDTO sendSmsDTO = new SendSmsDTO();
            sendSmsDTO.setPhoneNumbers(sysSmsCodeDTO.getReceiveTerminalNo());
            // todo 这边后期优化， code 为模板参数，固定的 最好再消息服务端设置好，客户端只需要传验证码值就行了
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",sysSmsCode.getVerificationCode());
            sendSmsDTO.setTemplateParam(jsonObject.toJSONString());
            sendSmsDTO.setBusinessType(sysSmsCodeDTO.getBusinessType());
            RestResponse<SendSmsVo> restResponse = aliyunSmsApi.sendSms(sendSmsDTO);
            if(ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE.equals(restResponse.getHeader().getMessage())){
                throw new BusinessException(ConstantUtil.ERROR,ConstantUtil.MESSAGE_SERVICE_NOT_AVAILABLE);
            }
            log.info("invoke alibaba send sms api,result:{} ",restResponse);
            if(ConstantUtil.ERROR.equals(restResponse.getHeader().getCode())){
                log.error("invoke alibaba send sms api,result:{} ",restResponse);
                throw new BusinessException(ConstantUtil.ERROR,restResponse.getHeader().getMessage());
            }
            sysSmsCodeVo.setSmsCodeId(sysSmsCode.getId());
            sysSmsCodeVo.setVerificationCode(sysSmsCode.getVerificationCode());
        }catch (Exception e){
            log.error("send sms error,reason:{}",e.getMessage());
            throw new BusinessException("102303",getMessage("102303"));
        }
        return sysSmsCodeVo;
    }

    /**
     * 验证验证码
     * @param sysSmsCodeDTO
     */
    @Override
    public void verifyCode(SysSmsCodeDTO sysSmsCodeDTO) {
        try {
            SysSmsCode smsCode = sysSmsCodeMapper.selectById(sysSmsCodeDTO.getSmsCodeId());
            if(smsCode==null){
                throw new BusinessException("102313",getMessage("102313"));
            }
            if (!sysSmsCodeDTO.getReceiveTerminalNo().equals(smsCode.getReceiveTerminalNo())) {
                // 终端号码匹配
                verifyFailure(smsCode,SmsCodeVerifyEnum.VERIFY_FAILURE.getCode(),getMessage("102306"));
                throw new BusinessException("102306",getMessage("102306"));
            } else if (!sysSmsCodeDTO.getBusinessType().equals(smsCode.getBusinessType())) {
                //业务类型匹配
                verifyFailure(smsCode, SmsCodeVerifyEnum.VERIFY_FAILURE.getCode(),getMessage("10237"));
                throw new BusinessException("102306",getMessage("102306"));
            } else if (!sysSmsCodeDTO.getVerificationCode().equals(smsCode.getVerificationCode())) {
                //验证码匹配
                verifyFailure(smsCode,SmsCodeVerifyEnum.VERIFY_FAILURE.getCode(),getMessage("102308"));
                throw new BusinessException("102306",getMessage("102306"));
            } else if (ConstantUtil.DELETE_FLAG_Y.equals(smsCode.getIsUsed())) {
                //是否已使用检查
                verifyFailure(smsCode,SmsCodeVerifyEnum.VERIFY_FAILURE.getCode(),getMessage("102309"));
                throw new BusinessException("102306",getMessage("102306"));
            } else if (DateUtil.between(new Date(),smsCode.getExpiredTime(),DateUnit.SECOND) < 0) {
                //是否过期检查(hutool date2-date1 date1>date2 说明过期）
                verifyFailure(smsCode,SmsCodeVerifyEnum.VERIFY_FAILURE.getCode(),getMessage("102310"));
                throw new BusinessException("102310",getMessage("102310"));
            } else {
                verifySuccess(smsCode);
                int i = sysSmsCodeMapper.updateById(smsCode);
                if (i == 0) {
                    verifyFailure(smsCode,SmsCodeVerifyEnum.VERIFY_FAILURE.getCode(),getMessage("102311"));
                    throw new BusinessException("102311",getMessage("102311"));
                }
            }
        } catch (Exception e) {
            log.error("verify code error reason:{}",e.getMessage());
            throw new BusinessException("102312",getMessage("102312"));
        }
    }


    /**
     * 消息验证码发送规则检查
     * @param sysSmsCodeDTO
     * @return
     */
    private void smsRuleCheck(SysSmsCodeDTO sysSmsCodeDTO){
        // 一天，同一个手机号最多只能发送 5 次 （默认查的都是未逻辑删除的）
        List<SysSmsCode> sysSmsCodeList = sysSmsCodeMapper.selectList(new QueryWrapper<SysSmsCode>()
                .eq(SysSmsCode.COL_RECEIVE_TERMINAL_NO,sysSmsCodeDTO.getReceiveTerminalNo())
                .eq(SysSmsCode.COL_RECEIVE_TERMINAL_TYPE,sysSmsCodeDTO.getReceiveTerminalType())
                .ge(SysSmsCode.COL_SEND_TIME, DateUtil.beginOfDay(new Date())));
        if(CollectionUtil.isNotEmpty(sysSmsCodeList)&& sysSmsCodeList.size()>SMS_COUNT){
            throw new BusinessException("102301",getMessage("102301"));
        }
        if(SmsTemplateTypeEnum.LOGIN.getCode().equals(sysSmsCodeDTO.getBusinessType())){
            // 登录：只有系统已存在的用户，才可以使用登录短信服务
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq(SysUser.COL_USERNAME,sysSmsCodeDTO.getReceiveTerminalNo()));
            if(sysUser==null){
                throw new BusinessException("102201",getMessage("102201"));
            }
        }else if(SmsTemplateTypeEnum.REG.getCode().equals(sysSmsCodeDTO.getBusinessType())){
            // 注册： 一天 限制 同一个ip 只能注册5个，即同一ip 只能发送5次注册短信
            List<SysUser> sysUserList = sysUserMapper.selectList(new QueryWrapper<SysUser>()
                    .eq(SysUser.COL_CLIENT_IP,sysSmsCodeDTO.getClientIp())
                    .ge(SysUser.COL_CREATE_TIME,DateUtil.beginOfDay(new Date())));
            if(CollectionUtil.isNotEmpty(sysUserList)&& sysUserList.size()>USER_COUNT){
                throw new BusinessException("102202",getMessage("102202"));
            }
        }else if(SmsTemplateTypeEnum.RESET_PWD.getCode().equals(sysSmsCodeDTO.getBusinessType())){
            // todo 重置密码： 次数做限制，同一天同一个用户重置密码次数不超过 5次

        }
        // 查该终端接收号码 最近一次发送未使用的验证码，如果发送时间不超过1分钟的，禁止再次发送，提示频繁发送
        SysSmsCode recentSmsCode = sysSmsCodeMapper.selectOne(new QueryWrapper<SysSmsCode>()
                .eq(SysSmsCode.COL_RECEIVE_TERMINAL_NO,sysSmsCodeDTO.getReceiveTerminalNo())
                .eq(SysSmsCode.COL_BUSINESS_TYPE,sysSmsCodeDTO.getBusinessType())
                .eq(SysSmsCode.COL_IS_SEND, ConstantUtil.DELETE_FLAG_Y)
                .eq(SysSmsCode.COL_IS_USED,ConstantUtil.DELETE_FLAG_N)
                .orderByDesc(SysSmsCode.COL_SEND_TIME).last("limit 1"));
        if(recentSmsCode!=null && DateUtil.between(recentSmsCode.getSendTime(),new Date(), DateUnit.MINUTE)<1){
            throw new BusinessException("102302",getMessage("102302"));
        }
    }

    private SysSmsCode genData(SysSmsCodeDTO sysSmsCodeDTO){
        // 生成随机验证码，业务存储验证码相关数据在前（避免分布式事务问题）
        String code = SmsCodeUtil.genVerificationCode();
        SysSmsCode sysSmsCode = new SysSmsCode();
        sysSmsCode.setId(new IdWorker().nextId());
        sysSmsCode.setReceiveTerminalType(sysSmsCodeDTO.getReceiveTerminalType());
        sysSmsCode.setReceiveTerminalNo(sysSmsCodeDTO.getReceiveTerminalNo());
        sysSmsCode.setExpiredTime(DateUtil.offsetMinute(new Date(),5));
        sysSmsCode.setVerificationCode(code);
        sysSmsCode.setBusinessType(sysSmsCodeDTO.getBusinessType());
        sysSmsCode.setClientIp(sysSmsCodeDTO.getClientIp());
        sysSmsCode.setPlatformType(sysSmsCodeDTO.getPlatformType());
        sysSmsCode.setIsSend(ConstantUtil.DELETE_FLAG_Y);
        sysSmsCode.setSendTime(new Date());
        sysSmsCode.setIsUsed(ConstantUtil.DELETE_FLAG_N);
        sysSmsCode.setDeleteFlag(ConstantUtil.DELETE_FLAG_N);
        sysSmsCode.setVersion(1);
        return sysSmsCode;
    }



    /**
     * 验证码校验失败
     * @param smsCode
     * @param code
     * @param message
     */
    private void verifyFailure(SysSmsCode smsCode,String code,String message){
        smsCode.setVerifyCode(code);
        smsCode.setVerifyMessage(message);
        sysSmsCodeMapper.updateById(smsCode);
    }

    /**
     * 校验成功数据组装
     * @param smsCode
     * @return
     */
    private SysSmsCode verifySuccess(SysSmsCode smsCode){
        smsCode.setUpdateTime(new Date());
        smsCode.setIsUsed(ConstantUtil.DELETE_FLAG_Y);
        smsCode.setUseTime(new Date());
        smsCode.setVerifyTime(new Date());
        smsCode.setVerifyCode(SmsCodeVerifyEnum.VERIFY_SUCCESS.getCode());
        smsCode.setVerifyMessage(SmsCodeVerifyEnum.VERIFY_SUCCESS.getDesc());
        return smsCode;
    }
}
