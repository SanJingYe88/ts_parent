package com.ts.article.dao;

import com.ts.article.pojo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentDao extends MongoRepository<Comment, String> {

    /**
     * 根据文章ID查询评论列表,分页,按照评论日期倒序
     *
     * @param articleid
     */
    Page<Comment> findByArticleidOrderByPublishdateDesc(String articleid, Pageable pageable);

    /**
     * 删除评论
     *
     * @param _id
     */
    void deleteCommentBy_id(String _id);
}
