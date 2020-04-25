package com.central.feign.common.interceptor;

import feign.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * Feign 拦截器
 * Description: 用于设置请求头，传递 Token
 * @author bin
 * @date 2020-04-21
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 设置请求头---如果token设置在请求头：Authorization 直接传递
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                requestTemplate.header(name, value);
            }
        }

        // 设置请求体，如果请求体中携带 access_token，放置下游feign接口中的请求头
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder body = new StringBuilder();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String value = request.getParameter(name);
                // 将 Token 加入请求头
                if ("access_token".equals(name)) {
                    requestTemplate.header("Authorization", "Bearer " + value);
                }
                // 其它参数加入请求体
                else {
                    body.append(name).append("=").append(value).append("&");
                }
            }
        }
        if(body.length()!=0) {
            body.deleteCharAt(body.length()-1);
            requestTemplate.body(body.toString());
            log.info("feign interceptor body:{}",body.toString());
        }
        // 设置请求体
//        if (body.length() > 0) {
//            // 去掉最后一位 & 符号
//            body.deleteCharAt(body.length() - 1);
//            requestTemplate.body(Request.Body.bodyTemplate(body.toString(), Charset.defaultCharset()));
//        }
    }
}