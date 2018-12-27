package com.ts.user.test;

import com.ts.user.pojo.User;
import com.ts.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void register() {
        User user = new User();
        user.setNickname("axxx");
        user.setMobile("16630371046");
        String code = "469043";
        userService.register(user,code);
    }

    @Test
    public void sendSms() {
        String mobile = "16630371046";
        userService.sendSms(mobile);
    }
}