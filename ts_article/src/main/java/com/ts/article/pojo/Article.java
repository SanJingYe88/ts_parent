package com.ts.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 文章实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "tb_article")
public class Article implements Serializable {
    @Id
    private String id;//ID
    private String columnid;//专栏ID
    private String userid;//用户ID
    private String title;//标题
    private String content;//文章正文
    private String image;//文章封面
    private Date createtime;//发表日期
    private Date updatetime;//修改日期
    private Integer ispublic;//是否公开 ( 0：不公开 1：公开 )
    private Integer istop;//是否置顶 ( 0：不置顶 1：置顶 )
    private Integer visits;//浏览量
    private Integer thumbup;//点赞数
    private Integer comment;//评论数
    private Integer state;//审核状态 ( 0：未审核 1：已审核 )
    private String channelid;//所属频道 ( 关联频道表ID )
    private String url;//URL
    private Integer type;//类型 ( 0：分享 1：专栏 )
}
