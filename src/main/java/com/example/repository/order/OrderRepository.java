package com.example.repository.order;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.Order;
import com.example.mapper.order.OrderMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository extends CrudRepository<OrderMapper, Order> {

}
