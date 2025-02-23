package com.example.exception.book;

import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public class BookTypeBindBookIsNotNullException extends BusinessException {
    public BookTypeBindBookIsNotNullException() {
        super(ErrorCode.BOOK_TYPE_BIND_BOOK_IS_NOT_NULL);
    }
}
