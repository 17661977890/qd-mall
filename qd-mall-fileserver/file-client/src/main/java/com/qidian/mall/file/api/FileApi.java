package com.qidian.mall.file.api;


import com.central.base.restparam.RestResponse;
import com.qidian.mall.file.hystrix.FileApiHystrix;
import com.qidian.mall.file.response.FileInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/file-center/uploadFile",method = RequestMethod.POST)
    RestResponse<FileInfoDTO> uploadFile(@RequestParam("file")MultipartFile multipartFile,@RequestParam("fileType") String fileType);
}
