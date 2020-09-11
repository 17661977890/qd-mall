package com.qidian.mall.file.hystrix;

import com.central.base.restparam.RestResponse;
import com.qidian.mall.file.api.FileApi;
import com.qidian.mall.file.response.FileInfoDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件api熔断处理
 * @author bin
 */
@Component
public class FileApiHystrix extends BaseFileHystrix implements FileApi {
    @Override
    public RestResponse<FileInfoDTO> uploadFile(MultipartFile multipartFile,String fileType) {
        return getError();
    }
}
