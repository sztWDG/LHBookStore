package com.example.exception.book;

import com.example.entity.enums.ErrorCode;
import com.example.exception.BusinessException;

public class BookNumberIsNotEnoughException extends BusinessException {
    public BookNumberIsNotEnoughException() {
        super(ErrorCode.BOOK_NUMBER_IS_NOT_ENOUGH);
    }
}
