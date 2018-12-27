package com.ts.user.test;

import com.ts.user.dao.AdminDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminDaoTest {

    @Autowired
    AdminDao adminDao;

    @Test
    public void countByLoginnameIs(){
        String loginname = null;
        int num = adminDao.countByLoginnameIs(loginname);
        log.info("{}",num);
    }
}
