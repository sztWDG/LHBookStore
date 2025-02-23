package com.example.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ENUM_VALUE_IS_NOT_DEFINE(40000,"枚举值没有被定义"),
    USERNAME_OR_EMAIL_EXISTS(40010,"用户名或邮箱已存在"),

    BOOK_ISBN_IS_EXISTS(40020,"该图书已存在"),
    BOOK_ISBN_IS_NOT_ALLOW_MODIFY(40021,"不允许修改图书Isbn"),
    BOOK_NUMBER_IS_NOT_ENOUGH(40022,"图书数量不足"),
    BOOK_TYPE_IS_EXISTS(40023,"图书种类已存在"),
    BOOK_TYPE_BIND_BOOK_IS_NOT_NULL(40024, "图书类型被绑定"),

    UNKNOWN_ERROR(-1, "未知错误"),
    PARAMETERS_ERROR(400, "参数错误"),
    ROLE_ERROR(403, "权限不足"),
    NO_LOGIN(401, "未登录"),
    SYSTEM_ERROR(500, "系统错误");


    private final int code;
    private final String message;

}
