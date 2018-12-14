package com.ts.base.service;

import com.ts.base.dao.LabelDao;
import com.ts.base.pojo.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional      //事务
public class LabelService {

    @Autowired
    LabelDao labelDao;

    @Autowired
    IdWorker idWorker;

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    //根据ID查询实体
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    //增加
    public void add(Label label){
        //使用雪花算法生成的ID
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    //修改
    public void update(Label label){
        labelDao.save(label);
    }

    //删除
    public void delete(String id){
        labelDao.deleteById(id);
    }

    //条件查询
    public List<Label> findSearch(Map map){
        Specification<Label> specification = this.createSpecification(map);
        return labelDao.findAll(specification);
    }

    //分页+条件查询
    public Page<Label> pageSearch(Map map, int page, int size){
        Specification<Label> specification = this.createSpecification(map);
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(specification,pageable);
    }

    //构造查询条件
    private Specification<Label> createSpecification(final Map map) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                // 标签ID
                if (map.get("id")!=null && !"".equals(map.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)map.get("id")+"%"));
                }
                // 标签名称
                if (map.get("labelname")!=null && !"".equals(map.get("labelname"))) {
                    predicateList.add(cb.like(root.get("labelname").as(String.class), "%"+(String)map.get("labelname")+"%"));
                }
                // 状态 ( 0-无效 , 1-有效 )
                if (map.get("state")!=null && !"".equals(map.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)map.get("state")+"%"));
                }
                // 是否推荐 ( 0-不推荐 , 1-推荐 )
                if (map.get("recommend")!=null && !"".equals(map.get("recommend"))) {
                    predicateList.add(cb.like(root.get("recommend").as(String.class), "%"+(String)map.get("recommend")+"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
