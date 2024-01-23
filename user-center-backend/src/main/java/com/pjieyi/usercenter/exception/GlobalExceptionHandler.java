package com.pjieyi.usercenter.exception;

import com.pjieyi.usercenter.common.BaseResponse;
import com.pjieyi.usercenter.common.ErrorCode;
import com.pjieyi.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author pjieyi
 * @description 捕获代码中所有的异常 ，内部消化，集中处理，让前端得到更详细的业务报错/信息
 * 同时屏蔽掉项目框架本身的异常，不暴露服务器内部状态
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getMessage()+","+e.getDescription(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
