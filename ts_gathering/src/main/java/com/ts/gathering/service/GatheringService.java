package com.ts.gathering.service;

import com.ts.gathering.dao.GatheringDao;
import com.ts.gathering.pojo.Gathering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.CheckUtils;
import util.DateUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Gathering 服务层
 */
@Slf4j
@Service
public class GatheringService {

    @Autowired
    private GatheringDao gatheringDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Gathering> findSearch(Map whereMap, int page, int size) {
        // 参数校验
        CheckUtils.check(page > 0, CheckUtils.CHECK_PARAM_DEFAULT_MSG, page);
        CheckUtils.check(size > 0, CheckUtils.CHECK_PARAM_DEFAULT_MSG, size);
        CheckUtils.notNullObj(whereMap,whereMap);
        Pageable pageable = PageRequest.of(page-1,size);
        Specification<Gathering> specification = this.createSpecification(whereMap);
        return gatheringDao.findAll(specification,pageable);
    }

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public Gathering findById(String id) {
        // 参数校验
        CheckUtils.notNullEmptyParam(id,id);
        return gatheringDao.findById(id).orElse(null);
    }

    /**
     * 增加
     * @param gathering
     */
    public void add(Gathering gathering) {
        // 参数校验
        CheckUtils.notNullObj(gathering,gathering);
        Date startTime = gathering.getStartTime();
        Date endTime = gathering.getEndTime();
        Date enrollTime = gathering.getEnrollTime();
        CheckUtils.notNullObj(startTime,gathering.getStartTime());   //开始时间不能为空
        CheckUtils.notNullObj(endTime,gathering.getEndTime());   //结束时间不能为空
        CheckUtils.notNullObj(enrollTime,gathering.getEnrollTime()); //截止时间不能为空
        //判断时间的合理性
        CheckUtils.check(!DateUtils.before(startTime,endTime),"开始时间不能晚于结束时间",startTime,endTime);
        CheckUtils.check(!DateUtils.before(enrollTime,startTime),"截止时间不能晚于开始时间",startTime,endTime);
        gathering.setId(idWorker.nextId() + "");
        gathering.setState(0);
        gatheringDao.save(gathering);
    }

    /**
     * 修改
     * @param gathering
     */
    public void update(Gathering gathering) {
        // 参数校验
        CheckUtils.notNullObj(gathering,gathering);
        gatheringDao.save(gathering);
    }

    /**
     * 删除活动(让活动不显示)
     * @param id
     */
    public void updateShowById(String id) {
        // 参数校验
        CheckUtils.notNullEmptyParam(id,id);
        gatheringDao.updateShowById(id);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<Gathering> createSpecification(final Map searchMap) {
        return new Specification<Gathering>() {
            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 编号
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 活动名称
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + (String) searchMap.get("name") + "%"));
                }
                //时间范围查询,要求同时提供开始时间和结束时间.
                String startTime = (String) searchMap.get("startTime");
                String endTime = (String) searchMap.get("endTime");
                log.info("startTime={}",startTime);
                log.info("endTime={}",endTime);
                boolean flagA = startTime != null && !"".equals(startTime);
                boolean flagB = endTime != null && !"".equals(endTime);
                if (flagA && flagB){        //存在开始时间,且存在结束时间,查询时间范围
                    Date date1 = DateUtils.parse(startTime);
                    Calendar calendar1 = DateUtils.date2Calender(date1, 0, 0, 0, 0);
                    Date date2 = DateUtils.parse(endTime);
                    Calendar calendar2 = DateUtils.date2Calender(date2, 23, 59, 59, 999);
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("startTime").as(Date.class), calendar1.getTime()));
                    predicateList.add(cb.lessThanOrEqualTo(root.get("endTime").as(Date.class), calendar2.getTime()));
                }else if(flagA && !flagB){  //存在开始时间,不存在结束时间,查询开始时间当天的数据
                    Date date1 = DateUtils.parse(startTime);
                    Calendar calendar1 = DateUtils.date2Calender(date1, 0, 0, 0, 0);
                    Date date2 = DateUtils.parse(startTime);
                    Calendar calendar2 = DateUtils.date2Calender(date2, 23, 59, 59, 999);
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("startTime").as(Date.class), calendar1.getTime()));
                    predicateList.add(cb.lessThanOrEqualTo(root.get("startTime").as(Date.class), calendar2.getTime()));
                }else if (flagB && !flagA){ //存在结束时间,不存在开始时间,查询结束时间当天的数据
                    Date date1 = DateUtils.parse(endTime);
                    Calendar calendar1 = DateUtils.date2Calender(date1, 0, 0, 0, 0);
                    Date date2 = DateUtils.parse(endTime);
                    Calendar calendar2 = DateUtils.date2Calender(date2, 23, 59, 59, 999);
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("endTime").as(Date.class), calendar1.getTime()));
                    predicateList.add(cb.lessThanOrEqualTo(root.get("endTime").as(Date.class), calendar2.getTime()));
                }
                // 主办方
                if (searchMap.get("sponsor") != null && !"".equals(searchMap.get("sponsor"))) {
                    predicateList.add(cb.like(root.get("sponsor").as(String.class), "%" + (String) searchMap.get("sponsor") + "%"));
                }
                // 举办地点
                if (searchMap.get("address") != null && !"".equals(searchMap.get("address"))) {
                    predicateList.add(cb.like(root.get("address").as(String.class), "%" + (String) searchMap.get("address") + "%"));
                }
                // 活动状态
                if (searchMap.get("state") != null) {
                    predicateList.add(cb.equal(root.get("state").as(Integer.class), searchMap.get("state")));
                }
                // 是否显示
                if (searchMap.get("canShow") != null) {
                    predicateList.add(cb.equal(root.get("canShow").as(Integer.class), searchMap.get("canShow")));
                }
                // 城市
                if (searchMap.get("city") != null && !"".equals(searchMap.get("city"))) {
                    predicateList.add(cb.like(root.get("city").as(String.class), "%" + (String) searchMap.get("city") + "%"));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }


    /**
     * 文件保存
     * @param path 文件保存路径
     * @param file 文件
     * @return 返回文件路径
     */
    public String saveFile(String path, MultipartFile file){
        //判断文件是否为空
        CheckUtils.check(file != null,CheckUtils.CHECK_PARAM_DEFAULT_MSG,file);
        CheckUtils.check(!file.isEmpty(),"文件内容为空",file);
        //TODO:判断文件格式
        File filePath = new File(path);
        log.info("文件保存路径:{}", path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            log.info("目录不存在,创建目录:{}", filePath);
            boolean mkdir = filePath.mkdir();
            if (!mkdir){
                throw new RuntimeException("目录创建失败");
            }
        }
        //获取原始文件名称
        String originalFileName = file.getOriginalFilename();
        log.info("原始文件名称:{}", originalFileName);
        //获取文件类型，以最后一个`.`作为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        log.info("文件类型:{}", type);
        //设置文件新名字
        String fileName = idWorker.nextId()+"."+type;
        log.info("文件新名称:{}", fileName);
        //在指定路径创建一个文件
        File targetFile = new File(path, fileName);
        //将文件保存到服务器指定位置
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.info("保存文件发生错误...");
            throw new RuntimeException("保存文件发生错误");
        }
        return path + "/" + fileName;
    }
}
