package com.example.exception.book;

import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public class BookIsbnIsExistsException extends BusinessException {
    public BookIsbnIsExistsException() {
        super(ErrorCode.BOOK_ISBN_IS_EXISTS);
    }
}
