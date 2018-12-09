package com.ts.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "tb_label")
public class Label implements Serializable{

    private String id;
    private String labelName;
    private String status;
    private Long count;
    private Long fans;
    private String recommend;
}
