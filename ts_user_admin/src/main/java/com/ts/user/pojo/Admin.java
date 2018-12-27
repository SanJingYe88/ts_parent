package com.ts.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Admin 实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_admin")
public class Admin implements Serializable {
    @Id
    private String id;          //ID
    private String loginname;   //登陆名称
    private String password;    //密码
    private String state;       //状态
    private Date createdate;    //创建时间
    private Date lastlogindate; //上次登录时间
}
