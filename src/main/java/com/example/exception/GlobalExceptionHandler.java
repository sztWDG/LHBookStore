package com.example.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.example.entity.RestBean;
import com.example.entity.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestBean<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error("{}", e.getMessage());
        return RestBean.error(ErrorCode.UNKNOWN_ERROR);
    }

    /**
     * 参数有误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public RestBean<String> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ) {
        e.printStackTrace();
        log.error("{}", e.getMessage());
        return RestBean.error(ErrorCode.PARAMETERS_ERROR);
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(NotRoleException.class)
    @ResponseBody
    public RestBean<String> handleNotRoleException(NotRoleException e) {
        e.printStackTrace();
        log.error("{}", e.getMessage());
        return RestBean.forbidden(e.getMessage());
    }

    /**
     * 未登录
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public RestBean<String> handleNotLoginException(NotLoginException e) {
        e.printStackTrace();
        log.error("{}", e.getMessage());
        return RestBean.unauthorized(e.getMessage());
    }


}
