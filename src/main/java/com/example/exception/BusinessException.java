package com.example.exception;

import com.example.entity.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
public class BusinessException extends RuntimeException {
    @Getter
    @Setter
    private int code;

    private String message;

    public String getMessage() {
        return Optional.ofNullable(super.getMessage()).orElse("");
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public BusinessException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }


}
