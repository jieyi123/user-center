package com.pjieyi.usercenter.common;

/**
 * @author pjieyi
 * @description  错误码
 */
public enum ErrorCode {

    SUCCESS(0,"ok",""),
    //400用户传入数据时出现错误
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    //500系统错误
    SERVER_ERROR(50000,"服务异常",""),
    SYSTEM_ERROR(50001,"系统内部异常",""),
    CAPTCHA_ERROR(-50005,"验证异常","");


    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码详情
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
