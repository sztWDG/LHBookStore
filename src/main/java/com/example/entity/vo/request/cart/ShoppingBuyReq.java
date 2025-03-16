package com.example.entity.vo.request.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingBuyReq {

    String itemId;
    long shoppingAddressId;
}
