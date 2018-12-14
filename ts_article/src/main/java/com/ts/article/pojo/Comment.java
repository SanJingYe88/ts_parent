package com.ts.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章评论 (mongo)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable{
    @Id
    private String _id;         // ID
    private String articleid;   // 所属文章ID
    private String content;     // 评论内容
    private String userid;      // 评论人ID
    private String nickname;    // 评论人的昵称
    private String parentid;    // 评论ID  如果为0表示文章的顶级评论
    private Date publishdate;   // 评论日期
}
