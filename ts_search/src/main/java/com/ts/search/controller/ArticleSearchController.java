package com.ts.search.controller;

import com.ts.search.pojo.Article;
import com.ts.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "/article")
public class ArticleSearchController {

    @Autowired
    ArticleSearchService articleSearchService;

    /**
     * 文章新增
     *
     * @param article
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Article article) {
        articleSearchService.add(article);
        return new Result(true, 20000, "添加成功");
    }

    /**
     * 文章搜索
     *
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{keywords}/{page}/{size}")
    public Result findByTitleOrContentLike(@PathVariable String keywords,
                                           @PathVariable int page,
                                           @PathVariable int size) {
        log.info("keywords={}", keywords);
        log.info("page={}", page);
        log.info("size={}", size);
        Page<Article> data = articleSearchService.findByTitleOrContentLike(keywords, keywords, page, size);
        return new Result(true, 20000, "搜索成功",
                new PageResult<>(data.getTotalElements(), data.getContent()));
    }

    /**
     * 中文搜索使用:
     * http://localhost:9007/article/search/中国/1/10
     * 且只会在浏览器中有效,在PostMan中无效.
     * 否则报错如下:
     * java.lang.IllegalArgumentException: Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 3986
     */
}
