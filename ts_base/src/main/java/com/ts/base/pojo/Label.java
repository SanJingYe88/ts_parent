package com.ts.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "tb_label")
public class Label implements Serializable{

    @Id
    private String id;          //标签ID
    @NotNull(message = "标签名称不能为空")
    private String labelname;   //标签名称
    private Integer state;       //状态 ( 0-无效 , 1-有效 )
    private Long count;         //使用数量
    private Integer recommend;   //是否推荐 ( 0-不推荐 , 1-推荐 )
    private Long fans;          //粉丝数
}
