package com.ts.qa.controller;

import entity.Result;
import entity.StatusCode;
import exception.JwtParserException;
import exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一异常处理类
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = JwtParserException.class)
    @ResponseBody
    public Result jwtParserException(Exception e) {
        e.printStackTrace();
        log.info("{}", e.getMessage());
        return new Result(false, StatusCode.TOKEN_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = PermissionException.class)
    @ResponseBody
    public Result permissionException(Exception e) {
        e.printStackTrace();
        log.info("{}", e.getMessage());
        return new Result(false, StatusCode.NO_PERMISSION, e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, "执行出错");
    }
}
