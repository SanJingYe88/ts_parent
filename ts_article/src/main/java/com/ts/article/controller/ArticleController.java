package com.ts.article.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.ts.article.pojo.Article;
import com.ts.article.service.ArticleService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.IdWorker;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private IdWorker idWorker;


    /**
     * 查询全部数据
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Article> list = articleService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        Article article = articleService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", article);
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Article> pageList = articleService.findSearch(searchMap, page, size);
        PageResult<Article> data = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", data);
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     */
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Map searchMap) {
        List<Article> list = articleService.findSearch(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 增加
     *
     * @param article
     */
    @PostMapping()
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param article
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Article article, @PathVariable String id) {
        article.setId(id);
        articleService.update(article);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        articleService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 文章审核
     *
     * @param id
     */
    @PutMapping(value = "/examine/{id}")
    public Result reviewArticle(@PathVariable String id) {
        articleService.reviewArticle(id);
        return new Result(true, StatusCode.OK, "审核成功");
    }

    /**
     * 文章点赞
     *
     * @param id
     */
    @PutMapping(value = "/thumbup/{id}")
    public Result approveArticle(@PathVariable String id) {
        articleService.approveArticle(id);
        return new Result(true, StatusCode.OK, "点赞成功");
    }
}
