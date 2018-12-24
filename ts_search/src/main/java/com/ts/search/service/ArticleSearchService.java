package com.ts.search.service;

import com.ts.search.dao.ArticleSearchDao;
import com.ts.search.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@Service
public class ArticleSearchService {

    @Autowired
    ArticleSearchDao articleSearchDao;

    @Autowired
    IdWorker idWorker;

    /**
     * 文章新增
     * @param article
     */
    public void add(Article article) {
        article.setId(idWorker.nextId() + "");
        log.info("{}", article);
        articleSearchDao.save(article);
    }

    /**
     * 文章搜索
     * @param title
     * @param content
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String title, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return articleSearchDao.findByTitleOrContentLike(title, content, pageable);
    }
}
