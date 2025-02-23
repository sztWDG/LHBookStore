package com.example.exception.book;

import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public class BookTypeNameIsExistsException extends BusinessException {
    public BookTypeNameIsExistsException() {
        super(ErrorCode.BOOK_TYPE_IS_EXISTS);
    }
}
