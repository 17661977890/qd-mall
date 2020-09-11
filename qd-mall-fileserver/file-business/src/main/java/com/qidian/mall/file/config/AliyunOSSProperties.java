package com.qidian.mall.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * oss 配置类
 * 使用@Component 或者 @EnableConfigurationProperties(AliyunOSSProperties.class)才可以注入
 * @author bin
 */
@Component
@ConfigurationProperties(prefix = "aliyun-oss")
@Data
public class AliyunOSSProperties {

    /**
     * 服务器地点
     */
    private String region;
    /**
     * 服务器地址
     */
    private String endpoint;
    /**
     * OSS身份id
     */
    private String accessKeyId;
    /**
     * 身份密钥
     */
    private String accessKeySecret;

    /**
     * 文件bucketName
     */
    private String bucketName;
    /**
     * 文件地址前缀
     */
    private String domainApp;
}

