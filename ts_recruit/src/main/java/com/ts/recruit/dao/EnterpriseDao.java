package com.ts.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.recruit.pojo.Enterprise;

import java.util.List;

public interface EnterpriseDao extends JpaRepository<Enterprise, String>, JpaSpecificationExecutor<Enterprise> {

    //热门企业列表(ishot=1)
    List<Enterprise> findByIshotEquals(String ishot);
}
