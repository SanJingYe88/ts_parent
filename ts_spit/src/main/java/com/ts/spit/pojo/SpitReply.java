package com.ts.spit.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 吐槽回复实体(楼中楼)
 */
public class SpitReply implements Serializable {

    @Id
    private String _id;         //主键,选择自己生成
    private String content;     //回复内容
    private Date publishtime;   //回复日期
    private String userid;      //回复人ID
    private String nickname;    //回复人昵称
    private int thumbup;        //点赞数
    private String state;       //是否可见(0-不可见,1-可见)
    private String toSpitId;    //被回复人的回复id(回复给那条回复的)
    private String toUserId;    //被回复人的id(回复给那条回复的)
    private String spitId;      //所属的吐槽
}
