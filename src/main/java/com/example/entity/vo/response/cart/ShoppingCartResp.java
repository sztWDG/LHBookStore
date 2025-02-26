package com.example.entity.vo.response.cart;

import com.example.entity.dto.ShoppingItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartResp {

    double totalPrice;
    List<ShoppingItem> items;


}
