package com.qidian.mall.user.controller;

import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestResponse;
import com.central.base.util.IpUtil;
import com.central.base.util.RegexUtil;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import com.qidian.mall.user.response.SysSmsCodeVo;
import com.qidian.mall.user.service.ISysSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author binsun
 * @Date 2020-09-15
 * @Description
 */
@Slf4j
@Api(tags = {"消息验证码web层"})
@RestController
@RequestMapping("/sys-sms-code")
public class SysSmsCodeController extends BaseController {

    @Autowired
    private ISysSmsService iSysSmsService;

    /**
     * 验证码发送
     * @param request
     * @param sysSmsCodeDTO
     * @return
     */
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @RequestMapping(value = "/sendSmsVerificationCode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse sendSmsVerificationCode(HttpServletRequest request, @Validated(SysSmsCodeDTO.Send.class)  @RequestBody SysSmsCodeDTO sysSmsCodeDTO){
        checkParam(sysSmsCodeDTO);
        sysSmsCodeDTO.setClientIp(IpUtil.getIpAddress(request));
        SysSmsCodeVo sysSmsCodeVo = iSysSmsService.sendSmsVerificationCode(sysSmsCodeDTO);
        log.info("response:{}",sysSmsCodeVo);
        return RestResponse.resultSuccess(sysSmsCodeVo);
    }



    /**
     * 入参检查
     * @param condition
     */
    private void checkParam(SysSmsCodeDTO condition){
        // 正则校验手机号或邮箱
        if(1==condition.getReceiveTerminalType()){
            // 手机
            boolean regex = RegexUtil.checkMobile(condition.getReceiveTerminalNo());
            if(!regex){
                throw new BusinessException("102304",getMessage("102304"));
            }
        }else {
            // 邮箱
            boolean regex = RegexUtil.checkMail(condition.getReceiveTerminalNo());
            if(!regex){
                throw new BusinessException("102305",getMessage("102305"));
            }
        }
    }

}
