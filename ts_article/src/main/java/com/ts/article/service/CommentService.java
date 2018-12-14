package com.ts.article.service;

import com.ts.article.dao.CommentDao;
import com.ts.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 新增文章评论
     *
     * @param comment
     */
    public void add(Comment comment) {
        comment.set_id(idWorker.nextId() + "");
        comment.setPublishdate(new Date());
        commentDao.save(comment);

        //TODO:文章评论数+1
    }

    /**
     * 根据文章ID查询评论列表,分页,按照评论日期倒序
     *
     * @param articleid
     */
    public Page<Comment> findByArticleid(String articleid, int page, int size) {
        PageRequest pageable = PageRequest.of(page-1,size);
        return commentDao.findByArticleidOrderByPublishdateDesc(articleid, pageable);
    }

    /**
     * 删除评论
     *
     * @param _id
     */
    public void deleteCommentBy_id(String _id) {
        //TODO: 更新文章的评论数

        commentDao.deleteCommentBy_id(_id);
    }
}
