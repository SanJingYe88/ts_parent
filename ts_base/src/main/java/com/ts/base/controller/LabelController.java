package com.ts.base.controller;

import com.ts.base.service.LabelService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin        //跨域
@RequestMapping(value = "label")
public class LabelController {

    @Autowired
    LabelService labelService;

    @GetMapping
    public Result findAll(){
        Result result = new Result(true, StatusCode.OK,"查询成功");
        result.setData(labelService.findAll());
        return result;
    }
}
