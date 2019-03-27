package com.ts.gathering.controller;

import entity.Result;
import entity.StatusCode;
import exception.CheckException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {

    // 处理参数验证异常
    @ExceptionHandler(value = CheckException.class)
    @ResponseBody
    public Result handleCheckException(CheckException e) {
        e.printStackTrace();
        String message = e.getMessage();
        return new Result(false, StatusCode.PARAM_ERROR, message);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, "执行出错");
    }
}
