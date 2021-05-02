package com.qidian.mall.uaa.controller;

import com.central.base.exception.BusinessException;
import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qidian.mall.uaa.websecurityconfig.smsprovider.SmsAuthenticationToken;
import com.qidian.mall.user.api.SysSmsCodeApi;
import com.qidian.mall.user.api.SysUserApi;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.uaa.websecurityconfig.mobileprovider.MobileAuthenticationToken;
import com.qidian.mall.uaa.websecurityconfig.openIdprovider.OpenIdAuthenticationToken;
import com.qidian.mall.user.request.SysSmsCodeDTO;
import com.qidian.mall.user.response.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * OAuth2相关操作
 * Spring Security 的认证过程3部曲：
 * （1）
 * （2）
 * （3）
 *
 * @author mall
 */
@Api(tags = "用户认证token相关")
@Slf4j
@RequestMapping(value = "/api/oauth")
@RestController
public class OAuth2Controller {
    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private TokenStore tokenStore;
    @Resource
    private SysUserApi sysUserApi;

    @Resource
    private SysSmsCodeApi sysSmsCodeApi;


    @Autowired
    private AuthenticationManager authenticationManager;

    @ApiOperation(value = "用户名密码获取token")
    @PostMapping("/user/token")
    public RestResponse getUserTokenInfo(@RequestBody SysUser umsAdminLoginParam,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (umsAdminLoginParam.getUsername() == null || "".equals(umsAdminLoginParam.getUsername())) {
            throw new UnapprovedClientAuthenticationException("用户名为空");
        }
        if (umsAdminLoginParam.getPassword() == null || "".equals(umsAdminLoginParam.getPassword())) {
            throw new UnapprovedClientAuthenticationException("密码为空");
        }
        //（1）username和password被获得后封装到一个UsernamePasswordAuthenticationToken（继承AbstractAuthenticationToken 而AbstractAuthenticationToken实现了Authentication接口，所以可以说是Authentication接口的实例）的实例中
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        return writerToken(request, response, token, "用户名或密码错误");
    }

    @ApiOperation(value = "openId获取token")
    @PostMapping("/openId/token")
    public void getTokenByOpenId(
            @ApiParam(required = true, name = "openId", value = "openId") String openId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId);
        writerToken(request, response, token, "openId错误");
    }

    @ApiOperation(value = "mobile获取token")
    @PostMapping("/mobile/token")
    public void getTokenByMobile(
            @ApiParam(required = true, name = "mobile", value = "mobile") String mobile,
            @ApiParam(required = true, name = "password", value = "密码") String password,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobileAuthenticationToken token = new MobileAuthenticationToken(mobile, password);
        writerToken(request, response, token, "手机号或密码错误");
    }

    @ApiOperation(value = "短信验证获取token")
    @PostMapping("/sms/token")
    public RestResponse getTokenBySms(@RequestBody @Validated({SysSmsCodeDTO.Verify.class}) SysSmsCodeDTO sysSmsCodeDTO,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 验证登录验证码
        SysSmsCodeDTO verify = new SysSmsCodeDTO();
        verify.setPlatformType(sysSmsCodeDTO.getPlatformType());
        verify.setBusinessType(sysSmsCodeDTO.getBusinessType());
        verify.setReceiveTerminalNo(sysSmsCodeDTO.getReceiveTerminalNo());
        verify.setReceiveTerminalType(sysSmsCodeDTO.getReceiveTerminalType());
        verify.setVerificationCode(sysSmsCodeDTO.getVerificationCode());
        verify.setSmsCodeId(sysSmsCodeDTO.getSmsCodeId());
        RestResponse<Boolean> restResponse = sysSmsCodeApi.verifyCode(verify);
        if (restResponse == null || restResponse.getBody() == null) {
            throw new BusinessException(ConstantUtil.ERROR,"短信验证失败");
        }
        if(ConstantUtil.USER_SERVICE_NOT_AVAILABLE.equals(restResponse.getHeader().getMessage())){
            throw new BusinessException(ConstantUtil.ERROR,ConstantUtil.USER_SERVICE_NOT_AVAILABLE);
        }
        SmsAuthenticationToken token = new SmsAuthenticationToken(sysSmsCodeDTO.getReceiveTerminalNo());
        return writerToken(request, response, token, "手机号错误");
    }


    /**
     * 获取登录用户信息 --- 测试feign拦截器（传递token 实现内部服务调用的token鉴权）
     * todo 使用spring cloud 做saas服务器时，经常会通过Feign调用远程服务。有时候我们的远程服务可能做了某些权限验证。
     *      需要验证header或者token什么的。如果某没有token，可能会被阻止调用。那如何添加token呢。
     *      如果每个方法都手动设置headers，那未免太麻烦。可以通过一个切面，自动帮我们添加请求header。
     * todo ：所以需要配置feign拦截  实现RequestInterceptor 重写apply方法
     *
     * todo： feign的实现有三种方式 httpurlconnection（默认） httpclient okhttp  我们可以在yml配置使用后面两种
     * @return
     */
    @ApiOperation(value = "获取登录用户信息")
    @PostMapping("/user/info")
    public RestResponse getLoginUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 通过feign远程调用用户中心的获取用户信息接口，来追加登录用户信息
        // 根据用户名查询 authentication.getName()
        RestResponse<UserInfoVO> restResponse  = sysUserApi.queryUserInfo(authentication.getName());
        UserInfoVO sysUser = restResponse.getBody();
//        LoginUserInfo loginUserInfo = new LoginUserInfo();
//        BeanUtils.copyProperties(sysUser,loginUserInfo);
//        return RestResponse.resultSuccess(loginUserInfo);
        return restResponse;
    }

    /**
     * 登出----移除token
     * 对于jwt 是不能移除的，因为jwt不会存在服务器，所以他的底层存储和移除方法都是空的，所以jwt在有效时间内都是有效的。
     * 移除方法针对
     * @return
     */
    @ApiOperation(value = "登出 remove token")
    @PostMapping("/user/logout")
    public RestResponse logout(HttpServletRequest request){
        String access_token = request.getParameter("access_token");
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(access_token);
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return RestResponse.resultSuccess(null);
    }


    /**
     * 自定义 token的认证方式以及获取令牌方式 （底层有密码模式 授权码模式 简化模式 客户端模式）我们这里有用户名密码和手机号 openId
     *  // 1. 从请求头中获取 ClientId
     *  // 2. 通过 ClientDetailsService 获取 ClientDetails
     *  // 3. 校验 ClientId和 ClientSecret的正确性
     *  // 4. 通过 TokenRequest构造器生成 TokenRequest
     *  // 5. 通过 TokenRequest的 createOAuth2Request方法获取 OAuth2Request
     *  // 6. 通过AuthenticationManager 调用自定义或者自带的验证机制链进行验证，填充Authentication
     *  // 6. 通过 Authentication和 OAuth2Request构造出 OAuth2Authentication
     *  // 7. 通过 AuthorizationServerTokenServices 生成 OAuth2AccessToken
     * @param request
     * @param response
     * @param token
     * @param badCredenbtialsMsg
     * @throws IOException
     */
    private RestResponse writerToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
            , String badCredenbtialsMsg) throws IOException {
        try {
            String clientId = request.getHeader("client_id");
            String clientSecret = request.getHeader("client_secret");
            if (clientId == null || "".equals(clientId)) {
                throw new UnapprovedClientAuthenticationException("请求头中无client_id信息");
            }

            if (clientSecret == null || "".equals(clientSecret)) {
                throw new UnapprovedClientAuthenticationException("请求头中无client_secret信息");
            }
            // 验证client 是否存在（我们对客户端信息做了redis优化）
            ClientDetails clientDetails = getClient(clientId, clientSecret);
            //封装client信息，底层会根据相关client的信息查询数据库来配置token 比如设置失效时间
            TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "customer");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            //（2）这个封装了登录验证信息（用户名密码或者手机/openId）的token被传递给AuthenticationManager进行验证，
            // 并调用provider验证机制，以及我们自行实现的查询用户信息的方法，检查认证成功后的返回一个得到完整填充的Authentication实例
            Authentication authentication = authenticationManager.authenticate(token);
            //（3）通过调用SecurityContextHolder.getContext().setAuthentication(...)，参数传递authentication对象，来建立安全上下文（security context）
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //（4）验证通过，我们会利用oauth2 来生成token，并发给前台用户
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            //调用底层token的生成方法（DefaultTokenServices实现了AuthorizationServerTokenServices接口提供的createAccessToken方法）
            OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            oAuth2Authentication.setAuthenticated(true);
//            writerObj(response, oAuth2AccessToken);
            return RestResponse.resultSuccess(oAuth2AccessToken);
        } catch (InternalAuthenticationServiceException e) {
            // 我们抛出的业务异常，如熔断服务调用失败，在底层会捕获并封装我们的业务异常信息，抛出InternalAuthenticationServiceException
            /**
             *          }catch (Exception var6) {
             *             throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
             *         }
             */
//            exceptionHandler(response, e);
            e.printStackTrace();
            return exceptionHandler(ConstantUtil.ERROR,e.getMessage());
        } catch (BadCredentialsException e){
            // 如果是用户密码信息校验失败 会抛出此异常BadCredentialsException 我们自定信息msg输出
//            exceptionHandler(response,badCredenbtialsMsg);
            e.printStackTrace();
            return exceptionHandler(ConstantUtil.ERROR,badCredenbtialsMsg);


        }catch (Exception e) {
            e.printStackTrace();
            return exceptionHandler(ConstantUtil.ERROR,e.getMessage());
//            exceptionHandler(response, e);

        }
    }

    private RestResponse exceptionHandler(String code,String msg) throws IOException {
        log.error("exceptionHandler-error:", msg);
        return RestResponse.resultError(code,msg);
    }


    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("exceptionHandler-error:", e);
        exceptionHandler(response, e.getMessage());
    }

    private void exceptionHandler(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writerObj(response, RestResponse.resultError(ConstantUtil.UNAUTHORIZED,msg));
    }

    private void writerObj(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (
                Writer writer = response.getWriter()
        ) {
            writer.write(objectMapper.writeValueAsString(obj));
            writer.flush();
        }
    }

    private ClientDetails getClient(String clientId, String clientSecret) {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        return clientDetails;
    }
}
