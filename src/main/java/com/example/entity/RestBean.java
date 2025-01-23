package com.example.entity;


import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public record RestBean<T>(int code, T data, String message) {
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "请求成功");
    }

    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, message);
    }

    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, message);
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    public static <T> RestBean<T> error(ErrorCode errorCode) {
        return failure(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> RestBean<T> error(BusinessException e) {
        return failure(e.getCode(), e.getMessage());
    }



}
