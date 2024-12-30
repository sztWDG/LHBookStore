package com.example.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(-1, "未知错误"),
    PARAMETERS_ERROR(400, "参数错误"),
    ROLE_ERROR(403, "权限不足"),
    NO_LOGIN(401, "未登录"),
    SYSTEM_ERROR(500, "系统错误");

    private final int code;
    private final String message;

}
