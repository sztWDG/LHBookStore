package com.example.entity.vo.response.book;

import com.example.entity.enums.BookStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BooksResp {
    Long id;
    String name;
    String author;
    String publisher;
    String isbn;
    BigDecimal price;
    int number;
    String cover;
    BookStatusEnum status;
    Long typeId;
    String description;
}
