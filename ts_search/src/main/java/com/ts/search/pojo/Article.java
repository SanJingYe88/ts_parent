package com.ts.search.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "ts", type = "article")
public class Article implements Serializable {

    @Id
    private String id;      //ID
    //是否索引,表示该字段是否可以被搜索
    //是否分词,表示搜索的时候是整体匹配还是单词匹配
    //是否存储,表示是否在页面上显示,一般写在这个pojo类中的属性都表示要存储
    //index=true,表示索引, analyzer = "ik_max_word" , searchAnalyzer = "ik_max_word"
    @Field(type = FieldType.Text, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;   //标题
    @Field(type = FieldType.Text, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content; //内容
    private String status;  //状态
}
