package com.example.exception.book;

import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public class BookIsbnIsNotAllowModifyException extends BusinessException {
    public BookIsbnIsNotAllowModifyException() {
        super(ErrorCode.BOOK_ISBN_IS_NOT_ALLOW_MODIFY);
    }
}
