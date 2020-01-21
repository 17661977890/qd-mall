package com.central.base.exception;

import com.central.base.restparam.RestResponse;
import com.central.base.restparam.RestResponseHeader;
import com.central.base.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类:------只针对业务异常 或者其他自定义、已知异常。有具体性
 * --@RestControllerAdvice 代替 @ControllerAdvice ，这样在方法上就可以不需要添加 @ResponseBody。
 * @Author 彬
 * @Date 2019/4/19
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常捕捉处理--------------这里暂不使用，太宽泛，不够具体
     * 我们在uaa 鉴权中心依赖此，spring security的自定义异常就会失效
     * httpSecurity.exceptionHandling()
     *                 .accessDeniedHandler(restfulAccessDeniedHandler)
     *                 .authenticationEntryPoint(restAuthenticationEntryPoint);
     * @param ex
     * @return
     */
//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public RestResponse errorHandler(Exception ex, HttpServletRequest request) {
//        RestResponse restResponse = new RestResponse();
//        RestResponseHeader restResponseHeader = new RestResponseHeader();
//        restResponseHeader.setCode(ConstantUtil.ERROR);
//        restResponseHeader.setMessage(ex.getMessage());
//        restResponse.setHeader(restResponseHeader);
//        Map map = new HashMap();
//        map.put("data",ConstantUtil.ERROR_DATA);
//        map.put("url",request.getRequestURI());
//        restResponse.setBody(map);
//        log.error("打印通用异常报错信息："+ex.getMessage()+";  具体请求："+request.getRequestURI());
//        ex.printStackTrace();
//        return restResponse;
//    }

    /**
     * 拦截捕捉自定义异常 BusinessException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public RestResponse myErrorHandler(BusinessException ex,HttpServletRequest request) {
        RestResponse restResponse = new RestResponse();
        RestResponseHeader restResponseHeader = new RestResponseHeader();
        restResponseHeader.setCode(ConstantUtil.ERROR);
        restResponseHeader.setMessage(ex.getMessage());
        restResponse.setHeader(restResponseHeader);
        Map map = new HashMap();
        map.put("data",ConstantUtil.ERROR_DATA);
        map.put("url",request.getRequestURI());
        restResponse.setBody(map);
        log.error("打印自定义异常报错信息 ："+ex.getMessage()+";  具体请求："+request.getRequestURI());
        ex.printStackTrace();
        return restResponse;
    }

    /**
     * 拦截参数校验异常
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RestResponse myErrorHandler2(MethodArgumentNotValidException ex,HttpServletRequest request) {
        String defaultMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        RestResponse restResponse = new RestResponse();
        RestResponseHeader restResponseHeader = new RestResponseHeader();
        restResponseHeader.setCode(ConstantUtil.ERROR);
        restResponseHeader.setMessage(defaultMessage);
        restResponse.setHeader(restResponseHeader);
        Map map = new HashMap();
        map.put("data",ConstantUtil.ERROR_DATA);
        map.put("url",request.getRequestURI());
        restResponse.setBody(map);
        log.error("打印参数校验异常报错信息 ："+defaultMessage+";  具体请求："+request.getRequestURI());
        ex.printStackTrace();
        return restResponse;
    }
}
