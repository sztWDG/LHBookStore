package com.example.service.cart;

import com.example.entity.dto.Order;
import com.example.entity.vo.request.cart.ModifyOrderStatusReq;
import com.example.processor.OrderProcessor;
import com.example.repository.order.OrderRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class OrderService{
    @Resource
    private OrderProcessor orderProcessor;
    @Resource
    private OrderRepository orderRepository;
    public boolean modifyOrderStatus(@Valid ModifyOrderStatusReq req) {

        //QxkQuestion:
        Order order = orderRepository.lambdaQuery().eq(Order::getOrderId, req.getOrderId()).one();
        return orderProcessor.process(order, req.getOrderEvents());
    }
}
