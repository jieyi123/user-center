package com.pjieyi.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pjieyi
 * @description 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -1679692206686286263L;
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description=description;
    }
    public BaseResponse(int code,T data,String message){
        this(code,data,message,"");
    }
    public BaseResponse(int code,T data){
        this(code,data,"","");
    }
    public BaseResponse(int code,String description){
        this.code=code;
        this.description=description;
    }
    public BaseResponse(int code,String message,String description){
        this.code=code;
        this.message=message;
        this.description=description;
    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(), errorCode.getDescription());
    }


}
