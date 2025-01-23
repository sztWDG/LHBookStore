package com.example.entity.vo.response.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)

public class ShippingAddressResp {

    long id;
    String name;
    String phone;
    String address;
}
