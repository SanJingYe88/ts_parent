package com.ts.spit.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 对吐槽的点赞,分享,浏览的操作历史记录表
 */
public class SpitOperate implements Serializable{
    @Id
    private String _id;
    private String userid;      //操作人ID
    private String nickname;    //操作人昵称
    private Date operatetime;     //操作时间
    private int operatetype;    //操作类型 (1-点赞,2-分享,3-浏览)
}
