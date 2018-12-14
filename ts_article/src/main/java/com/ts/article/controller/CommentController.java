package com.ts.article.controller;

import com.ts.article.pojo.Comment;
import com.ts.article.service.CommentService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 新增文章评论
     *
     * @param comment
     */
    @PostMapping
    public Result add(@RequestBody Comment comment) {
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 根据文章ID查询评论列表,分页,按照评论日期倒序
     *
     * @param articleid
     * @param page
     * @param size
     */
    @GetMapping(value = "/article/{articleid}/page/size")
    public PageResult<Comment> findByArticleid(@PathVariable String articleid,
                                               @PathVariable int page, @PathVariable int size) {
        Page<Comment> data = commentService.findByArticleid(articleid,page,size);
        return new PageResult<>(data.getTotalElements(),data.getContent());
    }

    /**
     * 删除评论
     *
     * @param _id
     */
    @DeleteMapping(value = "/{id}")
    public Result deleteCommentBy_id(@PathVariable String _id){
        commentService.deleteCommentBy_id(_id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
