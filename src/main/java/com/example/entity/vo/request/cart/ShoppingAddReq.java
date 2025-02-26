package com.example.entity.vo.request.cart;

import com.example.entity.dto.ItemBook;
import com.example.entity.dto.ShoppingItem;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = ShoppingItem.class)
public class ShoppingAddReq {
    Integer num;
    double itemPrice;
    ItemBook book;
}
