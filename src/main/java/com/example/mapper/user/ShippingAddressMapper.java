package com.example.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.ShippingAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShippingAddressMapper extends BaseMapper<ShippingAddress> {
}
