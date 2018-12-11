package com.ts.recruit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.ts.recruit.pojo.Recruit;
import com.ts.recruit.service.RecruitService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

/**
 * 控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping()
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Recruit> pageList = recruitService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Recruit>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param recruit
     */
    @PostMapping()
    public Result add(@RequestBody Recruit recruit) {
        recruitService.add(recruit);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param recruit
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Recruit recruit, @PathVariable String id) {
        recruit.setId(id);
        recruitService.update(recruit);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        recruitService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 推荐职位列表(state=2,以创建日期倒序,查询前10条)
     *
     * @return
     */
    @GetMapping(value = "/search/recommend")
    public Result recommendRecruitList() {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.recommendRecruitList());
    }

    /**
     * 最新职位列表(state!=0,以创建日期降序排序,查询前10条记录)
     *
     * @return
     */
    @GetMapping(value = "/search/newList")
    public Result newRecruitList() {
        return new Result(true, StatusCode.OK, "查询成功", recruitService.newRecruitList());
    }
}
