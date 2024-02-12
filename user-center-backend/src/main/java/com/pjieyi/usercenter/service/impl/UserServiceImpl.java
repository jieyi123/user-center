package com.pjieyi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pjieyi.usercenter.common.ErrorCode;
import com.pjieyi.usercenter.exception.BusinessException;
import com.pjieyi.usercenter.mapper.UserMapper;
import com.pjieyi.usercenter.model.User;
import com.pjieyi.usercenter.model.response.CaptureResponse;
import com.pjieyi.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.pjieyi.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.pjieyi.usercenter.constant.UserConstant.USER_LOGIN_STATUS;
import static com.pjieyi.usercenter.utils.AliyunIdentifyCode.getParams;

/**
* @author pjieyi
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-12-31 22:16:37
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{



    /**
     * 用户注册
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return  用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验输入参数
        //不能为空且不能为空串
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //账户不小于4位
        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户名太短");
        }
        //密码不小于6位
        if (userPassword.length()<6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        // 定义正则表达式，用户名只允许字母、数字和下划线
        String regex = "^[a-zA-Z0-9_]+$";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        //创建匹配器
        Matcher matcher = pattern.matcher(userAccount);
         if (! matcher.matches()){
             //有特殊字符存在
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名只允许字母、数字和下划线");
         }
        //密码和确认密码不相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //账户不能重复
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("userAccount",userAccount);
        if (this.count(wrapper)>0){
            //说明账户重复
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }

        //2.对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean result = this.save(user);
        if (!result){
            throw new BusinessException(ErrorCode.SERVER_ERROR,"服务异常");
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param request 记录用户登录状态
     * @return 脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request){
        //1.检验用户名和密码是否合法
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码不合法");
        }
        //账户不小于4位
        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户太短");
        }
        //密码不小于6位
        if (userPassword.length()<6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码太短");
        }
        // 定义正则表达式，用户名只允许字母、数字和下划线
        String regex = "^[a-zA-Z0-9_]+$";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        //创建匹配器
        Matcher matcher = pattern.matcher(userAccount);
        if (! matcher.matches()){
            //有特殊字符存在
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码有特殊字符");
        }
        //2.校验密码是否输入正确，要和数据库中的加密密码去对比
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = this.getOne(queryWrapper);

        if (user==null){
            log.info("user login failed,userAccount cannot match password");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误");
        }
        //3.信息处理
        User safeUser = getSafetyUser(user);
        //4.记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS,safeUser);
        return safeUser;
    }

    /**
     *
     * @param user 原始信息
     * @return 脱敏后的用户信息
     */
    @Override
    public   User getSafetyUser(User user) {
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        User safeUser=new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUserRole(user.getUserRole());
        return safeUser;
    }

    /**
     * 获取当前用户信息
     * @param request
     * @return 更新后的用户信息
     */
    @Override
    public User currentUser(HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //从session中只是取到当前用户的一个登录状态
        //如果在登录成功后对当前用户做一些更改 可能获取到的用户信息不一致
        //因此 需要从数据库中查询用户的最新信息返回
        currentUser = this.getById(currentUser.getId());
        User safetyUser = getSafetyUser(currentUser);
        request.getSession().setAttribute(USER_LOGIN_STATUS,safetyUser);
        return  safetyUser;
    }

    /**
     * 用户查询
     * @param username 用户名
     * @return 脱敏后的用户信息
     */
    @Override
    public List<User> searchUsers(String username,String phone,String email ,HttpServletRequest request){
        //1.鉴权 身份必须是管理员
        //获得当前登录的用户信息
        if (isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"必须是管理员");
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        //模糊查询
        if (StringUtils.isNoneBlank(username)){
            queryWrapper.like("username",username);
        }
        if (StringUtils.isNoneBlank(phone)){
            queryWrapper.like("phone",phone);
        }
        if (StringUtils.isNoneBlank(email)){
            queryWrapper.like("email",email);
        }
        List<User> userList = this.list(queryWrapper);
        //用户信息脱敏处理
        return userList.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }



    /**
     * 根据用户id删除用户（逻辑删除）
     * @param id 用户id
     * @return 结果
     */
    @Override
    public boolean deleteUser(long id, HttpServletRequest request){
        if (id<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id必须大于0");
        }
        //1.鉴权
        if (isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"必须是管理员");
        }
        return this.removeById(id);
    }

    /**
     * 图片二次验证
     * @param getParams 验证参数
     * @return 图形验证响应参数
     */
    @Override
    public CaptureResponse identifyCapture(Map<String, String> getParams) {
        JSONObject jsonObject = getParams(getParams);
        CaptureResponse captureResponse=new CaptureResponse();
        try {
            captureResponse.setResult(jsonObject.getString("result"));
            captureResponse.setStatus(jsonObject.getString("status"));
            captureResponse.setReason(jsonObject.getString("reason"));
            JSONObject captchaArgs = jsonObject.getJSONObject("captcha_args");
            String usedType = captchaArgs.getString("used_type");
            String userIp = captchaArgs.getString("user_ip");
            String scene = captchaArgs.getString("scene");
            String referer = captchaArgs.getString("referer");
            Map<String, String> captchaArgList = new HashMap<>();
            captchaArgList.put("usedType", usedType);
            captchaArgList.put("userIp", userIp);
            captchaArgList.put("scene", scene);
            captchaArgList.put("referer", referer);
            captureResponse.setCaptchaArgs(captchaArgList);
        }catch (RuntimeException e){
            throw new BusinessException(ErrorCode.CAPTCHA_ERROR);
        }
        return captureResponse;
    }

    /**
     * 是否是管理员
     * @param request 请求
     * @return 判定结果
     */
    private  boolean isAdmin(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        return sessionUser == null || sessionUser.getUserRole() != ADMIN_ROLE;
    }
}




