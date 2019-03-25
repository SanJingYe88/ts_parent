package com.ts.base.controller;

import entity.Result;
import entity.StatusCode;
import exception.CheckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.Response;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    // 处理所有接口数据验证异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new Result(false, StatusCode.ERROR, message);
    }

    // 处理参数验证异常
    @ExceptionHandler(value = CheckException.class)
    @ResponseBody
    public Result handleCheckException(CheckException e) {
        e.printStackTrace();
        String message = e.getMessage();
        return new Result(false, StatusCode.PARAM_ERROR, message);
    }

    @ExceptionHandler(value = Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}
