package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseEntity;
import com.example.entity.vo.response.user.ShippingAddressResp;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("shipping_address")
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = ShippingAddressResp.class)
public class ShippingAddress extends BaseEntity {
    @TableId(type = IdType.AUTO)
    long id;
    String name;
    String phone;
    String address;
    long userId;


}
