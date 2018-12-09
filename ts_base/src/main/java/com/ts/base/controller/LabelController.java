package com.ts.base.controller;

import com.ts.base.pojo.Label;
import com.ts.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    //标签全部列表
    @GetMapping
    public Result findAll() {
        Result result = new Result(true, StatusCode.OK, "查询成功");
        result.setData(labelService.findAll());
        return result;
    }

    //根据ID查询
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        Result result = new Result(true, StatusCode.OK, "查询成功");
        result.setData(labelService.findById(id));
        return result;
    }

    //增加标签
    @PostMapping
    public Result add(@RequestBody Label label) {
        Result result = new Result(true, StatusCode.OK, "新增成功");
        labelService.add(label);
        return result;
    }

    //推荐标签列表
    @GetMapping(value = "/topList")
    public Result topList() {
        Result result = new Result(true, StatusCode.OK, "查询成功");
        Map map = new HashMap();
        map.put("recommend", "1");
        result.setData(labelService.findSearch(map));
        return result;
    }

    //有效标签列表
    @GetMapping(value = "/list")
    public Result list() {
        Result result = new Result(true, StatusCode.OK, "查询成功");
        Map map = new HashMap();
        map.put("state", "1");
        result.setData(labelService.findSearch(map));
        return result;
    }

    //修改标签
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable String id, @RequestBody Label label) {
        Result result = new Result(true, StatusCode.OK, "修改成功");
        label.setId(id);
        labelService.update(label);
        return result;
    }

    //根据ID删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        Result result = new Result(true, StatusCode.OK, "删除成功");
        labelService.delete(id);
        return result;
    }

    //条件查询
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Map map) {
        Result result = new Result(true, StatusCode.OK, "查询成功");
        result.setData(labelService.findSearch(map));
        return result;
    }

    //条件查询 + 分页
    @PostMapping(value = "/search/{page}/{size}")
    public PageResult<Label> pageSearch(@PathVariable int page, @PathVariable int size, @RequestBody Map map) {
        PageResult<Label> pageResult = new PageResult<>();
        Page<Label> data = labelService.pageSearch(map, page, size);
        pageResult.setRows(data.getContent());
        pageResult.setTotal(data.getTotalElements());
        return pageResult;
    }
}
