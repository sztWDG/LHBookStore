package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.enums.OrderStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@TableName(value = "`order`")
public class Order {

    @TableId(type = IdType.AUTO)
    private long id;
    private String OrderId;
    private long userId;
    private OrderStatus status;
    private long bookId;
    private int boughtNumber;
    private double totalPrice;
    private long shippingAddressId;

    private LocalDateTime orderTime;
    @Nullable
    private LocalDateTime payTime;
    @Nullable
    private LocalDateTime deliveryTime;
    @Nullable
    private LocalDateTime receivedTime;
    @Nullable
    private LocalDateTime finishTime;

    private long createdBy;
    @Nullable
    private Long modifiedBy;

}
