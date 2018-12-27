package com.ts.user.test;

import com.ts.user.pojo.Admin;
import com.ts.user.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void add(){
        Admin admin = new Admin();
        admin.setLoginname("admin");
        admin.setPassword("admin");
        adminService.add(admin);
    }
}
