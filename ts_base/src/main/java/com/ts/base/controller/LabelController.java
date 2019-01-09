package com.ts.base.controller;

import com.ts.base.pojo.Label;
import com.ts.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

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
        log.info("result={}",result);
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

    @GetMapping("/test")
    public void test() throws InterruptedException {
        log.info("{}","----------");
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    Result result = restTemplate.getForObject("http://ts-base/label/1071765323784523776", Result.class);
                    log.info("result:{}.", result.getData());
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("请求完成");
    }

    @Autowired
    private RestTemplate restTemplate;
    // 总的请求个数
    private static final int requestTotal = 1000;
    // 同一时刻最大的并发线程的个数
    private static final int concurrentThreadNum = 20;
}
