package com.example.exception;

import com.example.entity.enums.ErrorCode;

public class EnumValueIsNotDefineException extends BusinessException{
    public EnumValueIsNotDefineException() {
        super(ErrorCode.ENUM_VALUE_IS_NOT_DEFINE);
    }
}
