package com.pjieyi.usercenter.controller;

import com.pjieyi.usercenter.common.BaseResponse;
import com.pjieyi.usercenter.common.ErrorCode;
import com.pjieyi.usercenter.common.ResultUtils;
import com.pjieyi.usercenter.exception.BusinessException;
import com.pjieyi.usercenter.model.User;
import com.pjieyi.usercenter.model.request.UserLoginRequest;
import com.pjieyi.usercenter.model.request.UserRegisterRequest;
import com.pjieyi.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.pjieyi.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @author pjieyi
 * @description 用户相关接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     * @param user 注册请求体
     * @return 用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest user){
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount=user.getUserAccount();
        String userPassword=user.getUserPassword();
        String checkPassword=user.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return  ResultUtils.success(result);
    }

    /**
     * 用户登录接口
     * @param user  登录请求体
     * @param request 获取用户状态
     * @return 用户信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest user,HttpServletRequest request){
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount=user.getUserAccount();
        String userPassword=user.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return  ResultUtils.success(result);
    }

    /**
     * 用户查询接口
     * @param username 用户名
     * @param phone 手机号
     * @param email 邮件
     * @param request 获取用户状态
     * @return 用户信息
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(@RequestParam(required = false) String username,@RequestParam(required = false)String phone,@RequestParam(required = false)String email, HttpServletRequest request){
        List<User> list = userService.searchUsers(username, phone,email,request);
        return ResultUtils.success(list);
    }

    /**
     * 删除用户接口（逻辑删除）
     * @param id 用户id
     * @param request 获取用户状态
     * @return 删除结果
     */
    @DeleteMapping
    public BaseResponse<Boolean> deleteUser(long id,HttpServletRequest request){
        if (id<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.deleteUser(id, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户信息
     * @param request 拿到session
     * @return 脱敏后的用户信息
     */
    @GetMapping("/current")
    public BaseResponse<User> currentUser(HttpServletRequest request){
        User result = userService.currentUser(request);
        return ResultUtils.success(result);
    }

    /**
     * 用户注销
     * @param request
     * @return 1-成功
     */
    @PostMapping("/outLogin")
    public BaseResponse<Integer> outLogin(HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //删除登录成功时存储的session对象
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return ResultUtils.success(1);
    }

}
