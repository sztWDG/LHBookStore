package com.example.entity.vo.request.book;

import com.example.entity.dto.Book;
import com.example.entity.enums.BookStatusEnum;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = Book.class)
public class BookSaveReq {
    String name;
    String author;
    String publisher;
    String isbn;
    double price;
    int number;
    @Nullable
    String cover;
    BookStatusEnum status;
    Long typeId;
    @Nullable
    String description;
}
