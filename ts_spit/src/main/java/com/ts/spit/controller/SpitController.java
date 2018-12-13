package com.ts.spit.controller;

import com.ts.spit.pojo.Spit;
import com.ts.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findOne(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findById(id));
    }

    /**
     * 增加
     *
     * @param spit
     */
    @PostMapping
    public Result add(@RequestBody Spit spit) {
        //TODO : 前台如果传过来的 parentid 有值,则是回复吐槽(非楼中楼)
        //TODO : 否则,就是发表吐槽.
        //TODO : 从session获取当前用户.
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "增加成功");
    }

//    /**
//     * 修改
//     *
//     * @param spit
//     */
//    @PutMapping(value = "/{id}")
//    public Result update(@RequestBody Spit spit, @PathVariable String id) {
//        spit.set_id(id);
//        spitService.update(spit);
//        return new Result(true, StatusCode.OK, "修改成功");
//    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable String id) {
        spitService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 根据上级ID查询吐槽,分页
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/comment/{parentId}/{page}/{size}")
    public PageResult<Spit> deleteById(@PathVariable String parentId,
                             @PathVariable int page, @PathVariable int size) {
        Page<Spit> data = spitService.findByParentid(parentId,page,size);
        return new PageResult<>(data.getTotalElements(),data.getContent());
    }

    /**
     * 点赞/取消点赞
     * @param id
     */
    @PutMapping(value = "/thumbup/{id}")
    public Result updateThumbup(@PathVariable String id) {
        //TODO : 获取用户数据,填写点赞人信息.
        spitService.updateThumbup(id);
        return new Result(true, StatusCode.OK, "点赞成功");
    }
}
