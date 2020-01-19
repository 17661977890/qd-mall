package com.qidian.mall.controller;

import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qidian.mall.entity.SysUser;
import com.qidian.mall.websecurityconfig.mobileprovider.MobileAuthenticationToken;
import com.qidian.mall.websecurityconfig.openIdprovider.OpenIdAuthenticationToken;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Api(tags = "OAuth2相关操作")
@Slf4j
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
    private AuthenticationManager authenticationManager;

    @ApiOperation(value = "用户名密码获取token")
    @PostMapping("/oauth/user/token")
    public void getUserTokenInfo(@RequestBody SysUser umsAdminLoginParam,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (umsAdminLoginParam.getUsername() == null || "".equals(umsAdminLoginParam.getUsername())) {
            throw new UnapprovedClientAuthenticationException("用户名为空");
        }
        if (umsAdminLoginParam.getPassword() == null || "".equals(umsAdminLoginParam.getPassword())) {
            throw new UnapprovedClientAuthenticationException("密码为空");
        }
        //（1）username和password被获得后封装到一个UsernamePasswordAuthenticationToken（继承AbstractAuthenticationToken 而AbstractAuthenticationToken实现了Authentication接口，所以可以说是Authentication接口的实例）的实例中
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        writerToken(request, response, token, "用户名或密码错误");
    }

    @ApiOperation(value = "openId获取token")
    @PostMapping("/oauth/openId/token")
    public void getTokenByOpenId(
            @ApiParam(required = true, name = "openId", value = "openId") String openId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId);
        writerToken(request, response, token, "openId错误");
    }

    @ApiOperation(value = "mobile获取token")
    @PostMapping("/oauth/mobile/token")
    public void getTokenByMobile(
            @ApiParam(required = true, name = "mobile", value = "mobile") String mobile,
            @ApiParam(required = true, name = "password", value = "密码") String password,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobileAuthenticationToken token = new MobileAuthenticationToken(mobile, password);
        writerToken(request, response, token, "手机号或密码错误");
    }

    private void writerToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
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
            writerObj(response, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            exceptionHandler(response, badCredenbtialsMsg);
            e.printStackTrace();
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }

    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("exceptionHandler-error:", e);
        exceptionHandler(response, e.getMessage());
    }

    private void exceptionHandler(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writerObj(response, RestResponse.resultError(ConstantUtil.ERROR,msg));
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
