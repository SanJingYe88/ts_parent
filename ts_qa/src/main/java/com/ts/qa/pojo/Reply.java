package com.ts.qa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="tb_reply")
public class Reply implements Serializable{

	@Id
	private String id;			//编号
	private String problemid;	//问题ID
	private String content;		//回答内容
	private Date createtime;	//创建日期
	private Date updatetime;	//更新日期
	private String userid;		//回答人ID
	private String nickname;	//回答人昵称
}
