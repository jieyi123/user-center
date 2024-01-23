package com.pjieyi.usercenter.exception;

import com.pjieyi.usercenter.common.ErrorCode;

/**
 * @author pjieyi
 * @description 自定义异常
 */
public class BusinessException extends RuntimeException{

    /**
     * 异常码
     */
    private final int code;
    /**
     * 描述
     */
    private final String description;

    public BusinessException(int code,String message,String description){
        super(message);
        this.code=code;
        this.description=description;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description= errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode,String description){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description= description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
