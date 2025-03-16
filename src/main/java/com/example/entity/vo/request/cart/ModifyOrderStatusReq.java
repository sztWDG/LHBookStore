package com.example.entity.vo.request.cart;

import com.example.entity.enums.OrderEvents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyOrderStatusReq {
    String orderId;
    OrderEvents orderEvents;

}
