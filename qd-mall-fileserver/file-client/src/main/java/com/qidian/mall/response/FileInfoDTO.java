package com.qidian.mall.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件信息出参
 * @author bin
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileInfoDTO implements Serializable {

    private String path;

}
