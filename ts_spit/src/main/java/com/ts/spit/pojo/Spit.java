package com.ts.spit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 吐槽实体(非楼中楼)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spit implements Serializable {
    @Id
    private String _id;         //主键,选择自己生成
    private String content;     //吐槽内容
    private Date publishtime;   //发布日期
    private Date replytime;     //最后回复时间
    private String userid;      //发布人ID
    private String nickname;    //发布人昵称
    private int visits;         //浏览量
    private int thumbup;        //点赞数
    private int share;          //分享数
    private int comment;        //回复数
    private String state;       //是否可见(0-不可见,1-可见)
    private String parentid;    //上级ID(-1表示不是吐槽回复)
}
