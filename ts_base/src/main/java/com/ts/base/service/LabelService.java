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
import util.CheckUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public Label findById(String id){
        // 参数校验
        CheckUtils.notNullEmptyParam(id,id);
        return labelDao.findById(id).orElse(null);
    }

    /**
     * 增加
     * @param label
     */
    public void add(Label label){
        //使用雪花算法生成的ID
        label.setId(idWorker.nextId() + "");
        label.setFans(0L);
        label.setCount(0L);
        labelDao.save(label);
    }

    /**
     * 修改
     * @param label
     */
    public void update(Label label){
        // 参数校验
        CheckUtils.notNull(label,CheckUtils.CHECK_PARAM_DEFAULT_MSG,label);
        CheckUtils.notNullEmptyParam(label.getId(),label.getId());
        labelDao.save(label);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id){
        // 参数校验
        CheckUtils.notNullEmptyParam(id,id);
        labelDao.deleteById(id);
    }

    /**
     * 条件查询
     * @param map
     * @return
     */
    public List<Label> findSearch(Map map){
        Specification<Label> specification;
        if (map != null){
            specification = this.createSpecification(map);
            return labelDao.findAll(specification);
        }
        return null;
    }

    /**
     * 分页+条件查询
     * @param map
     * @param page
     * @param size
     * @return
     */
    public Page<Label> pageSearch(Map map, int page, int size){
        // 参数校验
        CheckUtils.check(page > 0, CheckUtils.CHECK_PARAM_DEFAULT_MSG, page);
        CheckUtils.check(size > 0, CheckUtils.CHECK_PARAM_DEFAULT_MSG, size);
        Specification<Label> specification;
        Pageable pageable = PageRequest.of(page-1,size);
        if (map != null){
            specification = this.createSpecification(map);
            return labelDao.findAll(specification,pageable);
        }
        return labelDao.findAll(pageable);
    }

    /**
     * 构造查询条件
     * @param map
     * @return
     */
    private Specification<Label> createSpecification(final Map map) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> searchList = new ArrayList<>();
                // 标签ID
                if (map.get("id")!=null && !"".equals(map.get("id"))) {
                    searchList.add(cb.like(root.get("id").as(String.class), "%"+(String)map.get("id")+"%"));
                }
                // 标签名称
                if (map.get("labelname")!=null && !"".equals(map.get("labelname"))) {
                    searchList.add(cb.like(root.get("labelname").as(String.class), "%"+(String)map.get("labelname")+"%"));
                }
                // 状态 ( 0-无效 , 1-有效 )
                if (map.get("state")!=null && !"".equals(map.get("state"))) {
                    searchList.add(cb.equal(root.get("state").as(Integer.class),map.get("state")));
                }
                // 是否推荐 ( 0-不推荐 , 1-推荐 )
                if (map.get("recommend")!=null && !"".equals(map.get("recommend"))) {
                    searchList.add(cb.equal(root.get("recommend").as(Integer.class), map.get("recommend")));
                }
                return cb.and(searchList.toArray(new Predicate[searchList.size()]));
            }
        };
    }

    /**
     * 批量删除
     * @param ids 要删除的ids,例如: 1,5,9,10
     * @return
     */
    public void batchDelete(String ids) {
        //参数校验
        CheckUtils.notNullEmptyParam(ids,ids);
        //把IN条件中的拼接的字符串改为数组或List格式传入,否则报错
        List<String> idList = Arrays.asList(ids.split(","));
        labelDao.batchDeleteIn(idList);
    }

    /**
     * 批量修改
     * @param ids 要删除的ids,例如: 1,5,9,10
     * @param operateColumn 要修改的字段
     * @param newValue 修改为的新值
     * @return
     */
    public void batchUpdate(String ids,String operateColumn, Object newValue) {
        //参数校验
        CheckUtils.notNullEmptyParam(ids,ids);
        CheckUtils.notNullEmptyParam(operateColumn,operateColumn);
        CheckUtils.notNull(newValue,CheckUtils.CHECK_PARAM_DEFAULT_MSG,newValue);
        //把IN条件中的拼接的字符串改为数组或List格式传入,否则报错
        List<String> idList = Arrays.asList(ids.split(","));
        if (operateColumn.equals("state")){
            labelDao.batchUpdateState(idList,newValue);
        }else if (operateColumn.equals("recommand")){
            labelDao.batchUpdateRecommand(idList,newValue);
        }
    }
}
