package com.example.entity.vo.request.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingBuyAllReq {

    List<String> itemIds;
    long shoppingAddressId;
}
