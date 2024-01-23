package com.pjieyi.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pjieyi.usercenter.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author pjieyi
* @description 针对表【user】的数据库操作Service
* @createDate 2023-12-31 22:16:37
*/
public interface UserService extends IService<User> {

     String SALT="pjy";

    /**
     * 用户注册
     * @param username 用户名
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return  用户id
     */
     long userRegister(String username,String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param username 用户名
     * @param userPassword 密码
     * @param request 记录用户登录状态
     * @return 脱敏后的用户信息
     */
    User userLogin(String username, String userPassword, HttpServletRequest request);

    /**
     * 获取当前用户信息
     * @param request 当前用户
     * @return 更新后的用户信息
     */
    User currentUser(HttpServletRequest request);

    /**
     *
     * @param user 原始信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User user);

    /**
     * 用户查询
     * @param username 用户名
     * @return 脱敏后的用户信息
     */
    List<User> searchUsers(String username, String phone,String email,HttpServletRequest request);

    /**
     * 根据用户id删除用户（逻辑删除）
     * @param id 用户id
     * @return 结果
     */
    boolean deleteUser(long id, HttpServletRequest request);
}
