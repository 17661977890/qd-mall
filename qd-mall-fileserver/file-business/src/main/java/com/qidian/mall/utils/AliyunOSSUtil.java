package com.qidian.mall.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.central.base.exception.BusinessException;
import com.central.base.util.ConstantUtil;
import com.qidian.mall.config.AliyunOSSProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * oss 工具类
 * @author bin
 */
@Component
public class AliyunOSSUtil {

    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    private static Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    private OSS init(){
        OSS ossClient = new OSSClientBuilder().build(aliyunOSSProperties.getEndpoint(), aliyunOSSProperties.getAccessKeyId(), aliyunOSSProperties.getAccessKeySecret());
        return ossClient;
    }

    /**
     * 创建存储空间
     *
     * param ossClient OSS连接
     * param bucketName 存储空间
     * return
     */
    public String createBucketName(String bucketName) {
        // 存储空间
        OSS ossClient = init();
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketName;
    }
    /**
     * 删除存储空间buckName
     *
     * param ossClient oss对象
     * param bucketName 存储空间
     */
    public void deleteBucket(String bucketName) {
        OSS ossClient = init();
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     *
     * param ossClient oss连接
     * param bucketName 存储空间
     * param folder 模拟文件夹名如"qj_nanjing/"
     * return 文件夹名
     */
    public String createFolder(String bucketName, String folder) {
        // 文件夹名
        OSS ossClient = init();
        // 判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, folder)) {
            // 创建文件夹
            ossClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            // 得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, folder);
            return object.getKey();
        }
        return folder;
    }


    /**
     * 根据key删除OSS服务器上的文件
     *
     * param ossClient: oss连接
     * param bucketName: 存储空间
     * param folder: 模拟文件夹名 如"qj_nanjing/"
     * param key: Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public void deleteFile(String bucketName, String folder, String key) {
        OSS ossClient = init();
        ossClient.deleteObject(bucketName, folder + key);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }


    /**
     * 上传文件到阿里云 OSS 服务器
     *
     * @param file
     * @param fileType
     * @param bucketName
     * @param storagePath 存储文件夹目录
     * @return
     */
    public String uploadFile(MultipartFile file, String fileType, String bucketName, String storagePath) {
        //创建OSSClient实例
        OSS ossClient = init();
        //创建一个唯一的文件名，类似于id，就是保存在OSS服务器上文件的文件名(自定义文件名)
        String fileName = file.getOriginalFilename();
        try {
            assert fileName != null;
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            fileName = UUID.randomUUID() +"-"+fileType+ "." + suffix+".";
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            //设置数据流里有多少个字节可以读取
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            fileName = StringUtils.isNotBlank(storagePath) ? storagePath + "/" + fileName : fileName;
            //上传文件
            PutObjectResult result = ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);
            logger.info("Aliyun OSS AliyunOSSUtil.uploadFileToAliyunOSS,result:{}", result);
        } catch (IOException e) {
            logger.error("Aliyun OSS AliyunOSSUtil.uploadFileToAliyunOSS fail,reason:{}",e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return aliyunOSSProperties.getDomainApp()+fileName;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @param bucketName
     */
    public void deleteFile(String fileName, String bucketName) {
        OSS ossClient = init();
        ossClient.deleteObject(bucketName, fileName);
        shutdown(ossClient);
    }

    /**
     * 根据图片全路径删除就图片
     *
     * @param imgUrl     图片全路径
     * @param bucketName 存储路径
     */
    public void delImg(String imgUrl, String bucketName) {
        if (StringUtils.isBlank(imgUrl)) {
            return;
        }
        //问号
        int index = imgUrl.indexOf('?');
        if (index != -1) {
            imgUrl = imgUrl.substring(0, index);
        }
        int slashIndex = imgUrl.lastIndexOf('/');
        String fileId = imgUrl.substring(slashIndex + 1);
        OSS ossClient = init();
        ossClient.deleteObject(bucketName, fileId);
        shutdown(ossClient);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName   文件名称
     * @param bucketName 文件储存空间名称
     * @return true:存在,false:不存在
     */
    public boolean doesObjectExist(String fileName, String bucketName) {
        Validate.notEmpty(fileName, "fileName can be not empty");
        Validate.notEmpty(bucketName, "bucketName can be not empty");
        OSS ossClient = init();
        try {
            return ossClient.doesObjectExist(bucketName, fileName);
        } finally {
            shutdown(ossClient);
        }

    }

    /**
     * 复制文件
     *
     * @param fileName              源文件名称
     * @param bucketName            源储存空间名称
     * @param destinationBucketName 目标储存空间名称
     * @param destinationObjectName 目标文件名称
     */
    public void ossCopyObject(String fileName, String bucketName, String destinationBucketName, String destinationObjectName) {
        Validate.notEmpty(fileName, "fileName can be not empty");
        Validate.notEmpty(bucketName, "bucketName can be not empty");
        Validate.notEmpty(destinationBucketName, "destinationBucketName can be not empty");
        Validate.notEmpty(destinationObjectName, "destinationObjectName can be not empty");
        OSS ossClient = init();
        // 拷贝文件。
        try {
            ossClient.copyObject(bucketName, fileName, destinationBucketName, destinationObjectName);
        } catch (OSSException oe) {
            logger.error("errorCode:{},Message:{},requestID:{}", oe.getErrorCode(), oe.getMessage(), oe.getRequestId());
            /**
             * 文件不存在
             */
            String NO_SUCH_KEY = "NoSuchKey";
            /**
             * 存储空间不存在
             */
            String NO_SUCH_BUCKET = "NoSuchBucket";
            if (oe.getErrorCode().equals(NO_SUCH_KEY)) {
                throw new BusinessException(ConstantUtil.ERROR, NO_SUCH_KEY);
            } else if (oe.getErrorCode().equals(NO_SUCH_BUCKET)) {
                throw new BusinessException(ConstantUtil.ERROR, NO_SUCH_BUCKET);
            } else {
                throw new BusinessException(ConstantUtil.ERROR,oe.getMessage());
            }
        } finally {
            shutdown(ossClient);
        }
    }

    /**
     * 关闭oos
     *
     * @param ossClient ossClient
     */
    private void shutdown(OSS ossClient) {
        ossClient.shutdown();
    }
}

