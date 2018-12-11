package com.ts.recruit.pojo;

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
 * 职位类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "tb_recruit")
public class Recruit implements Serializable {

    @Id
    private String id;          //ID
    private String jobname;     //职位名称
    private String salary;      //薪资范围
    private String condition;   //经验要求
    private String education;   //学历要求
    private String type;        //任职方式
    private String address;     //办公地址
    private String eid;         //企业ID
    private Date createtime;    //创建日期
    private String state;       //状态 (0：关闭 1:开启 2：推荐)
    private String url;         //网址
    private String label;       //标签
    private String content1;    //职位描述
    private String content2;    //职位要求
}
