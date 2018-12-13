package com.ts.spit.service;

import com.ts.spit.dao.SpitDao;
import com.ts.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 查询全部记录
     *
     * @return
     */

    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 增加
     *
     * @param spit
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId() + "");
        spit.setVisits(0);  //浏览量
        spit.setThumbup(0); //点赞数
        spit.setShare(0);   //分享数
        spit.setComment(0); //回复数
        spit.setState("1"); //是否可见(0-不可见,1-可见)
        spit.setPublishtime(new Date());    //发布日期

        //判断是回复吐槽(非楼中楼),还是发表吐槽.
        //回复吐槽
        if (spit.getParentid() != null || !"".equals(spit.getParentid().trim())){
            //回复数+1
            this.incrNum(spit.getParentid(),"comment",1,"spit");
            //TODO : 对吐槽的点赞,分享,浏览的操作历史记录表中做相应的记录
        }

        //保存
        spitDao.save(spit);
    }

    /**
     * 根据主键查询实体
     *
     * @param id
     * @return
     */
    public Spit findById(String id) {

        //TODO : 对吐槽的点赞,分享,浏览的操作历史记录表中添加浏览记录

        //该条吐槽的浏览量+1
        this.incrNum(id,"visits",1,"spit");

        return spitDao.findById(id).get();
    }

    /**
     * 修改
     *
     * @param spit
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {

        //TODO : 逻辑删除所有属于该条吐槽的回复, 点赞信息, 分享信息, 浏览信息等

        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽列表
     *
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return spitDao.findByParentid(parentid, pageable);
    }

    /**
     * 更新点赞. 点赞/取消点赞
     *
     * @param id
     */
    public void updateThumbup(String id) {
        //TODO : 重复点赞,待处理.
        //TODO : 创建一个点赞,分享,浏览的操作历史记录表.
        //TODO : 判断是否已操作过, 如果有,则取消点赞, 否则添加点赞记录.并将点赞数+1

        if(true){  //还没有点赞
            //点赞数+1
            this.incrNum(id,"thumbup",1,"spit");
            //TODO : 记录点赞
        }else { //已经点赞
            //点赞数-1
            this.incrNum(id,"thumbup",-1,"spit");
            //TODO : 记录取消点赞
        }
    }

    /**
     * 根据 _id 属性是 key 的记录的 field 域的值 + num, 操作的集合是 collectionName
     * 浏览数+1,回复数+1,点赞数+1
     * @param key
     * @param field
     * @param collectionName
     */
    public void incrNum(String key,String field,int num, String collectionName) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(key));
        Update update = new Update().inc(field, num);
        mongoTemplate.updateFirst(query, update, collectionName);
    }
}
