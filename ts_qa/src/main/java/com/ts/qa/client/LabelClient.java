package com.ts.qa.client;

import com.ts.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component      //该注解不是必须的,但是不使用的话,IDEA会显示一个烦人的错误提示.
@FeignClient(value = "ts-base",fallback = LabelClientImpl.class)
public interface LabelClient {

    //根据ID查询
    @RequestMapping(value = "/label/{id}",method = RequestMethod.GET)
    Result findById(@PathVariable(value = "id") String id);
}
