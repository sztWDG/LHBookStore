package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.cart.ModifyOrderStatusReq;
import com.example.service.cart.OrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderService orderService;


    @PutMapping
    public RestBean<Boolean> modifyOrderStatus(@RequestBody @Valid ModifyOrderStatusReq req) {

        return RestBean.success(orderService.modifyOrderStatus(req));
    }



}
