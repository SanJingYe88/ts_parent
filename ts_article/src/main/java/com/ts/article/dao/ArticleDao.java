package com.ts.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据访问接口
 */
public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    //文章审核
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "update tb_article set state = ?2 where id = ?1", nativeQuery = true)
    void updateStateById(String id, String state);

    //文章点赞
    @Modifying
    @Transactional(readOnly = false)
    @Query(value = "update tb_article set thumbup = thumbup + 1 where id = ?1", nativeQuery = true)
    void updateThumbupById(String id);

    //文章评论数 +1 / -1
    @Modifying
    @Transactional
    @Query(value = "update tb_article set comment = comment + ?2 where id = ?1", nativeQuery = true)
    void updateComment(String id, int num);
}
