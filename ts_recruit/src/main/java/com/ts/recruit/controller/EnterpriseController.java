package com.ts.recruit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.ts.recruit.pojo.Enterprise;
import com.ts.recruit.service.EnterpriseService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

/**
 * 控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 查询全部数据
     *
     * @return
     */

    @GetMapping()
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", enterpriseService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", enterpriseService.findById(id));
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
        Page<Enterprise> pageList = enterpriseService.findSearch(searchMap, page, size);
        PageResult data = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", data);
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", enterpriseService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param enterprise
     * @return
     */
    @PostMapping()
    public Result add(@RequestBody Enterprise enterprise) {
        enterpriseService.add(enterprise);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param enterprise
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Enterprise enterprise, @PathVariable String id) {
        enterprise.setId(id);
        enterpriseService.update(enterprise);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        enterpriseService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 热门企业列表(ishot=1)
     *
     * @return
     */
    @GetMapping(value = "/search/hotList")
    public Result hotList() {
        return new Result(true, StatusCode.OK, "查询成功", enterpriseService.hotList());
    }
}
