package com.ts.gathering.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Gathering 活动实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "tb_gathering")
public class Gathering implements Serializable {

    @Id
    private String id;              //ID
    private String name;            //活动名称
    private String summary;         //活动简介
    private String detail;          //详细说明
    private String sponsor;         //主办方
    private String image;           //活动图片
    @Column(name = "start_time")
    private Date startTime;         //开始时间
    @Column(name = "end_time")
    private Date endTime;           //截止时间
    @Column(name = "enroll_time")
    private Date enrollTime;        //报名截止时间
    private String city;            //城市
    private String address;         //举办地点
    private Integer state;           //活动状态,(0-待审核,1-审核通过,2-审核不通过)
    @Column(name = "canshow")
    private Integer canShow;        //是否可见,(0-不可见,1-可见)
}