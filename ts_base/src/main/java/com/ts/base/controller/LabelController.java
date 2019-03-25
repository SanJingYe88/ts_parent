package com.ts.base.controller;

import com.ts.base.pojo.Label;
import com.ts.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin        //跨域
@RequestMapping(value = "label")
public class LabelController {

    @Autowired
    LabelService labelService;

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        log.info("id={}",id);
        return new Result(true, StatusCode.OK, "查询成功",labelService.findById(id));
    }

    /**
     * 增加标签
     * @param label
     * @return
     */
    @PostMapping
    public Result add(@Validated @RequestBody Label label) {
        log.info("label={}",label);
        labelService.add(label);
        return new Result(true, StatusCode.OK, "新增成功");
    }

    /**
     * 推荐标签列表
     * @return
     */
    @GetMapping(value = "/topList")
    public Result topList() {
        Map map = new HashMap(1);
        map.put("recommend", "1");
        return new Result(true, StatusCode.OK, "查询成功", labelService.findSearch(map));
    }

    /**
     * 有效标签列表
     * @return
     */
    @GetMapping(value = "/list")
    public Result list() {
        Map map = new HashMap(1);
        map.put("state", "1");
        return new Result(true, StatusCode.OK, "查询成功", labelService.findSearch(map));
    }

    /**
     * 修改标签
     * @param id
     * @param label
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable String id, @RequestBody Label label) {
        log.info("id={}",id);
        log.info("label={}",label);
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        log.info("id={}",id);
        labelService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 条件查询 + 分页
     * @param page
     * @param size
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result pageSearch(@PathVariable() int page, @PathVariable int size, @RequestBody(required = false) Map searchMap) {
        log.info("page={}",page);
        log.info("size={}",size);
        log.info("searchMap={}",searchMap);
        Page<Label> data = labelService.pageSearch(searchMap, page, size);
        PageResult<Label> pageResult = new PageResult<>(data.getTotalElements(),data.getContent());
        return new Result(true, StatusCode.OK, "查询成功",pageResult);
    }

    /**
     * 批量删除
     * @param map
     * @return
     */
    @DeleteMapping(value = "/batchDelete")
    public Result batchDelete(@RequestBody Map map) {
        String ids = (String) map.get("ids");
        log.info("ids={}",ids);
        labelService.batchDelete(ids);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 批量修改
     * @param map
     * @return
     */
    @PutMapping(value = "/batchUpdate")
    public Result batchUpdate(@RequestBody Map map) {
        String ids = (String) map.get("ids");
        String operateType = (String) map.get("operateType");
        Integer newValue = (Integer) map.get("newValue");
        log.info("ids={}",ids);
        log.info("operateType={}",operateType);
        log.info("newValue={}",newValue);
        labelService.batchUpdate(ids,operateType,newValue);
        return new Result(true, StatusCode.OK, "修改成功");
    }
}
