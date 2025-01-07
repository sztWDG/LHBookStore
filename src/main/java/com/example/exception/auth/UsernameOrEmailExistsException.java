package com.example.exception.auth;

import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public class UsernameOrEmailExistsException extends BusinessException {
    public UsernameOrEmailExistsException() {
        super(ErrorCode.USERNAME_OR_EMAIL_EXISTS);
    }
}
