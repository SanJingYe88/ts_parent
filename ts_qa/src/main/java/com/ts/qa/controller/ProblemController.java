package com.ts.qa.controller;

import com.ts.qa.client.LabelClient;
import com.ts.qa.pojo.Problem;
import com.ts.qa.service.ProblemService;
import com.ts.qa.util.JwtUtil;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 问答控制器层
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private LabelClient labelClient;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        Problem problem = problemService.findById(id);
        if (problem == null){
            return new Result(true, StatusCode.NOT_FOUND, "查询成功");
        }
        return new Result(true, StatusCode.OK, "查询成功", problem);
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
        Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param problem
     */
    @PostMapping
    public Result add(@RequestBody Problem problem) {
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.update(problem);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        problemService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 最新回答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/newList/{labelId}/{page}/{size}")
    public Result newListByLabelId(@PathVariable String labelId,
                                   @PathVariable int page, @PathVariable int size) {
        Page<Problem> data = problemService.findNewListByLabelId(labelId, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(data.getTotalElements(), data.getContent()));
    }

    /**
     * 热门回答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/hotList/{labelId}/{page}/{size}")
    public Result hotListByLabelId(@PathVariable String labelId,
                                   @PathVariable int page, @PathVariable int size) {
        Page<Problem> data = problemService.findHotListByLabelId(labelId, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(data.getTotalElements(), data.getContent()));
    }

    /**
     * 等待回答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/waitList/{labelId}/{page}/{size}")
    public Result waitListByLabelId(@PathVariable String labelId,
                                    @PathVariable int page, @PathVariable int size) {
        Page<Problem> data = problemService.findWaitListByLabelId(labelId, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(data.getTotalElements(), data.getContent()));
    }

    @PostMapping(value = "/publish")
    public Result publishProblem(@RequestBody Problem problem) {
        //检查权限
        checkPermission("user");
        //保存
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "发布成功");
    }

    /**
     * 微服务调用,调用 ts-base 里的 LabelController 中的方法
     * @param labelId
     * @return
     */
    @GetMapping(value = "/label/{labelId}")
    public Result findLabelById(@PathVariable(value = "labelId") String labelId){
        log.info("{}",labelId);
        Result result = labelClient.findById(labelId);
        return new Result(true, StatusCode.OK, "查询成功",result);
    }

    /**
     * 权限检查
     *
     * @param needRole 需要的角色
     */
    private void checkPermission(String needRole) {
        //获取角色
        String hasRole = (String) request.getAttribute("roles");
        log.info("hasRole={}", hasRole);
        if (hasRole == null || !hasRole.equals(needRole)) {
            throw new PermissionException("权限不足");
        }
    }
}
