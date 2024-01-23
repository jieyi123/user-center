package com.pjieyi.usercenter.common;

/**
 * @author pjieyi
 * @description 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     * @param data 数据
     * @return
     * @param <T>
     */
      public static <T> BaseResponse<T> success(T data){
          return new BaseResponse<>(0,data,"ok");
      }

    /**
     * 失败
     * @param errorCode
     * @return
     */
      public static BaseResponse error(ErrorCode errorCode){
          return new BaseResponse<>(errorCode);
      }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),message,description);
    }

    /**
     * 失败
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse<>(errorCode.getCode(),description);
    }

    /**
     *失败
     * @param code 状态码
     * @param message 状态信息
     * @param description 状态详情
     * @return
     */
    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,message,description);
    }

}
