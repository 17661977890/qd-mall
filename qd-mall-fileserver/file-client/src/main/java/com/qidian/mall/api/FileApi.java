package com.qidian.mall.api;


import com.central.base.restparam.RestResponse;
import com.qidian.mall.hystrix.FileApiHystrix;
import com.qidian.mall.response.FileInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理 feign接口
 * @author bin
 */
@FeignClient(value = "qd-mall-fileserver",configuration = FeignClientsConfiguration.class,fallback = FileApiHystrix.class)
public interface FileApi {

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    @RequestMapping("/file-center/uploadFile")
    RestResponse<FileInfoDTO> uploadFile(@RequestParam("file")MultipartFile multipartFile);
}
