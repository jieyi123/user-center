package com.pjieyi.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pjieyi
 * @description 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 7861371674190266188L;

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;
}
