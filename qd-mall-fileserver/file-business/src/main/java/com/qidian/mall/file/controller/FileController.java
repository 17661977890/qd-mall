package com.qidian.mall.file.controller;

import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestResponse;
import com.qidian.mall.file.config.AliyunOSSProperties;
import com.qidian.mall.file.response.FileInfoDTO;
import com.qidian.mall.file.utils.AliyunOSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理controller
 */
@Slf4j
@Api(tags = {"文件处理web"})
@RequestMapping("/file-center")
@RestController
public class FileController extends BaseController {

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    private static final String USER_FILE_PATH = "user-file";

    /**
     * 文件上传
     * @param
     * @return 是否添加成功
     */
    @ApiOperation(value = "文件上传", notes = "文件处理-文件上传")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public RestResponse<FileInfoDTO> uploadFile(@RequestParam("file") MultipartFile multipartFile,@RequestParam("fileType") String fileType) {
        String path = aliyunOSSUtil.uploadFile(multipartFile, fileType,aliyunOSSProperties.getBucketName(),USER_FILE_PATH);
        return new RestResponse().success(new FileInfoDTO(path));
    }
}
