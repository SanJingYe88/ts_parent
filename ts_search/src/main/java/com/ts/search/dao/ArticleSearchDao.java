package com.ts.search.dao;

import com.ts.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 数据访问接口
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {

    /**
     * 文章搜索
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
