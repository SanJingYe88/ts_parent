package com.ts.gathering.pojo;

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
 * 活动类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "tb_gathering")
public class Gathering implements Serializable {

    @Id
    private String id;              //编号
    private String name;            //活动名称
    private String summary;         //大会简介
    private String detail;          //详细说明
    private String sponsor;         //主办方
    private String image;           //活动图片
    private Date starttime;         //开始时间
    private Date endtime;           //截止时间
    private String address;         //举办地点
    private Date enrolltime;        //报名截止
    private String state;           //是否可见
    private String city;            //城市
}
