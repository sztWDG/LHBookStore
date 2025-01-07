package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseEntity;
import com.example.entity.enums.BookStatusEnum;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("book")
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {
    @TableId(type = IdType.AUTO)
    Long id;
    String name;
    String author;
    String publisher;
    String isbn;
    BigDecimal price;
    int number;
    @Nullable
    String cover;
    BookStatusEnum status;
    Long typeId;
    @Nullable
    String description;
}
