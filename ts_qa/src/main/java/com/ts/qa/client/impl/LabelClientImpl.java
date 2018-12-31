package com.ts.qa.client.impl;

import com.ts.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LabelClientImpl implements LabelClient{

    @Override
    public Result findById(String id) {
        log.info("{}","熔断器....");
        return new Result(false, StatusCode.ERROR,"出错了,请稍后再试");
    }
}
