package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseEntity;
import com.example.entity.enums.BookStatusEnum;
import com.example.entity.vo.response.book.BooksResp;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("book")
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = BooksResp.class)
public class Book extends BaseEntity {
    @TableId(type = IdType.AUTO)
    long id;
    String name;
    String author;
    String publisher;
    String isbn;
    double price;
    int number;
    @Nullable
    String cover;
    BookStatusEnum status;
    long typeId;
    @Nullable
    String description;
}
