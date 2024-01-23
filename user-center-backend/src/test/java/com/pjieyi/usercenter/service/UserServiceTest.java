package com.pjieyi.usercenter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author  pjieyi
 * @description  UserService类测试
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testUserRegister(){
        String username="";
        String password="";
        String checkPassword="";
        //测试为空
        Assertions.assertEquals(-1, userService.userRegister(username, password, checkPassword));
        //账号不小于4位
        username="123";
        password="123";
        checkPassword="123";
        Assertions.assertEquals(-1, userService.userRegister(username, password, checkPassword), "用户名小于四位");
        username="12345";
        Assertions.assertEquals(-1, userService.userRegister(username, password, checkPassword), "密码小于六位");
        username="q 3";
        password="123456";
        checkPassword="123456";
        Assertions.assertEquals(-1, userService.userRegister(username, password, checkPassword), "账户没有特殊字符");
        username="1234";
        password="123456";
        checkPassword="12345678";
        Assertions.assertEquals(-1, userService.userRegister(username, password, checkPassword), "两次密码输入一致");
        username="zhangsan";
        checkPassword="123456";
        Assertions.assertEquals(-1, userService.userRegister(username, password, checkPassword), "账户不重复");
        username="1234567";
        Assertions.assertTrue(userService.userRegister(username,password,checkPassword)>0,"新增失败");
    }

}