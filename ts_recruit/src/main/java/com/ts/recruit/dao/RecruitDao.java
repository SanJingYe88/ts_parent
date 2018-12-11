package com.ts.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.recruit.pojo.Recruit;

import java.util.List;

public interface RecruitDao extends JpaRepository<Recruit, String>, JpaSpecificationExecutor<Recruit> {

    //推荐职位列表(state=2,以创建日期倒序,查询前10条)
    List<Recruit> findTop10ByStateOrderByCreatetimeDesc(String state);

    //最新职位列表(state!=0,以创建日期降序排序,查询前10条记录)
    List<Recruit> findTop10ByStateNotOrderByCreatetimeDesc(String state);
}
