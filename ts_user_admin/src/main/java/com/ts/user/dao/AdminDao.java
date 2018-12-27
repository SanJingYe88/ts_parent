package com.ts.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.user.pojo.Admin;

/**
 * 数据访问接口
 */
public interface AdminDao extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {

    /**
     * 根据loginname统计数量
     * @param loginname 用户名
     * @return
     */
    int countByLoginnameIs(String loginname);

    /**
     * 根据loginname查询
     * @param loginname 用户名
     * @return
     */
    Admin findByLoginnameIs(String loginname);
}
