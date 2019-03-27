package com.ts.gathering.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.gathering.pojo.Gathering;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据访问接口
 */
public interface GatheringDao extends JpaRepository<Gathering, String>, JpaSpecificationExecutor<Gathering> {

    /**
     * 设置活动是否显示
     * @param id
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE tb_gathering SET canshow = CASE WHEN canshow = 1 THEN 0 ELSE 1 END WHERE id = ?1")
    void updateShowById(String id);
}
