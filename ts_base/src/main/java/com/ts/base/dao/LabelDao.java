package com.ts.base.dao;

import com.ts.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Native;
import java.util.List;

/**
 * Label DAO 接口
 */
public interface LabelDao extends JpaRepository<Label, String> , JpaSpecificationExecutor<Label>{

    @Modifying
    @Transactional()
    @Query(nativeQuery = true,value = "DELETE FROM Label w  where w.id IN ( ?1 )")
    void batchDeleteIn(@Param("idList") List<String> idList);

    // 注意, JPA中, 如果要使用 IN 关键字进行批量操作, 需要把IN条件中的拼接的字符串改为数组或List格式传入,否则报错.
    // 原因是：jpa和hibernate在进行参数替换的时候是使用占位符的形式，防止了sql的注入，在解析会给参数带上单引号.
    @Modifying
    @Transactional()
    @Query(nativeQuery = true, value = "UPDATE tb_label SET state = ?2 where id IN ?1")
    void batchUpdateState(@Param("idList") List<String> idList,@Param("newValue") Object newValue);

    @Modifying
    @Transactional()
    @Query(nativeQuery = true, value = "UPDATE tb_label SET recommand = ?2 where id IN ?1")
    void batchUpdateRecommand(@Param("idList") List<String> idList,@Param("newValue") Object newValue);
}
