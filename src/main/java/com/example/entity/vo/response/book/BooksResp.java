package com.example.entity.vo.response.book;

import com.example.entity.enums.BookStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BooksResp {
    long id;
    String name;
    String author;
    String publisher;
    String isbn;
    double price;
    int number;
    String cover;
    BookStatusEnum status;
    Long typeId;
    String description;
}
