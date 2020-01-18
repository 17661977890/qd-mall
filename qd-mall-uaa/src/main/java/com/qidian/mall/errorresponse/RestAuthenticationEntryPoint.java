package com.qidian.mall.errorresponse;


import cn.hutool.json.JSONUtil;
import com.central.base.restparam.RestResponse;
import com.central.base.util.ConstantUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * https://github.com/shenzhuan/mallplus on 2018/5/14.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.toJsonStr(new RestResponse().error(ConstantUtil.UNAUTHORIZED,"暂未登录或token已经过期",authException.getMessage())));
        response.getWriter().flush();
    }
}
