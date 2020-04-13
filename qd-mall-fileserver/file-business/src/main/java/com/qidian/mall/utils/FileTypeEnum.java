package com.qidian.mall.utils;

public enum FileTypeEnum {

    IMG(1, "图片"),
    AUDIO(2, "音频"),
    VIDEO(3, "视频"),
    APP(4, "App包"),
    OTHER(5, "其他");

    private Integer code;
    private String message;

    FileTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

