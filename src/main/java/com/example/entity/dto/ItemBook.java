package com.example.entity.dto;

import com.example.entity.enums.BookStatusEnum;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBook {
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
    Long typeId;
    @Nullable
    String description;
}
