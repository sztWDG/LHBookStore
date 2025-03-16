package com.example.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
