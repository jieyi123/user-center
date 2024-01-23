package com.pjieyi.usercenter.model.request;

import lombok.Data;

/**
 * @author pjieyi
 * @description 用户登录请求体
 */
@Data
public class UserLoginRequest {
    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

}
