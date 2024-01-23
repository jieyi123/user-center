package com.pjieyi.usercenter;

import com.pjieyi.usercenter.model.User;
import com.pjieyi.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private UserService userService;
    @Test
     void testMybatisPlus(){
        User user=new User();
        user.setUsername("zhangsan1");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.baidu.com");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("13222332222");
        user.setEmail("13213");
        user.setUserStatus(0);
        user.setIsDelete(0);
        boolean result = userService.save(user);
        Assertions.assertTrue(result);
        System.out.println(user.getId());
    }

    @Test
     void testCommonsLang3(){
        String s=null;
        boolean result= StringUtils.isAnyBlank(s);
        Assertions.assertTrue(result);
        s="";
        result=StringUtils.isAnyBlank(s);
        Assertions.assertTrue(result);
    }

    @Test
    void testValidAccountName(){
        String userName="123";
        //验证账户不能包含特殊字符
        // 定义正则表达式，只允许字母、数字和下划线
        String regex = "^[a-zA-Z0-9_]+$";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        //创建匹配器
        Matcher matcher = pattern.matcher(userName);
        boolean matches = matcher.matches();
        Assertions.assertTrue(matches);
        //Assert.isTrue(matches,"账户不能有特殊字符");
    }

    @Test
    void testHashPassword() throws NoSuchAlgorithmException {
        final String salt="pjy";
        String newPassword = DigestUtils.md5DigestAsHex((salt + "1234567").getBytes());
        System.out.println(newPassword);
        Assertions.assertTrue(true);
    }
}
