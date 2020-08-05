package com.qidian.mall.search.aspect;

import com.alibaba.fastjson.JSON;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 基础日志记录
 * @author: sunbin
 *
 **/
public class BaseControllerLogAspect {

    public void logSystemBefore(Logger logger, JoinPoint joinPoint){
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes == null){
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String url = this.validLog(request.getRequestURL().toString());
        String httpMethod = this.validLog(request.getMethod());
        String ip = this.validLog(request.getRemoteAddr());
        String classMethod = this.validLog(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        String args = this.validLog(JSON.toJSONString(getParams(joinPoint.getArgs())));
        // 记录下请求内容
        logger.info("URL : {}", url );
        logger.info("HTTP_METHOD : {}", httpMethod);
        logger.info("IP : {}", ip);
        logger.info("CLASS_METHOD : {}", classMethod);
        logger.info("ARGS : {}", args);
    }

    public void logSystemAfter(Logger logger, Object ret, Long end){
        String response = JSON.toJSONString(ret);
        logger.info("RESPONSE : {}", response);
        String spendTime = (System.currentTimeMillis() - end) + "MS";
        logger.info("SPEND TIME : {}", spendTime);
    }

    private  String validLog(String log){
        List<String> list = new ArrayList<>();
        list.add("%0d");
        list.add("\r");
        list.add("%0a");
        list.add("\n");
        String encode = Normalizer.normalize(log,Normalizer.Form.NFKC);
        for(int i=0;i<list.size();i++){
            encode = encode.replace(list.get(i),"");
        }
        return encode;
    }

    private List<Object> getParams(Object[] args){
        List<Object> params = new ArrayList<>();
        if(args!=null&&args.length>0){
            for (int i = 0 ; i < args.length; i++) {
                Object o =  args[i];
                if(!(o instanceof HttpServletRequest || o instanceof ResponseFacade || o instanceof MultipartFile)){
                    params.add(o);
                }
            }
        }
        return params;
    }
}
