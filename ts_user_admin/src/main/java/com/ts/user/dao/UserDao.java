package com.ts.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.user.pojo.User;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 统计该手机号的记录
     * @param mobile
     * @return
     */
    int countByMobileIs(String mobile);

    /**
     * 根据手机号码查询用户
     * @param mobile
     * @return
     */
    User findByMobileIs(String mobile);
}
