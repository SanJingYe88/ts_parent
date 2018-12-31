package com.ts.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    //最新回答列表(按回复时间降序排序)
    @Query("select p from Problem p" +
            " where id in ( select problemid from Pl where labelid=?1 )" +
            " order by replytime desc")
    Page<Problem> findNewListByLabelId(String labelId,Pageable pageable);

    //热门回答列表(按回复量降序排序,指定一个时间范围,比如3天内)
    @Query(value = "select * from tb_Problem" +
            " where id in ( select problemid from tb_Pl where labelid=?1 )" +
            " and DATEDIFF(createtime,NOW()) <= 0 and DATEDIFF(createtime,NOW()) > -?2" +
            " order by reply desc",nativeQuery = true)
//    @Query("select p from Problem p" +
//            " where id in (select problemid from Pl where labelid=?1 )" +
//            " and DATEDIFF(createtime,NOW()) <= 0 and DATEDIFF(createtime,NOW()) > -3" +
//            " order by reply desc")
    Page<Problem> findHotListByLabelId(String labelId,int dayBefore,Pageable pageable);

    //等待回答列表(按照问题发布时间倒序)
    @Query(value = "select * from tb_Problem" +
            " where id in ( select problemid from tb_Pl where labelid=?1 )" +
            " and reply = 0 " +
            " order by createtime desc",nativeQuery = true)
    Page<Problem> findByLabelIdAndReplyOrderByCreatetimeDesc(String labelId,Long reply,Pageable pageable);
}
