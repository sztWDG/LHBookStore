package com.example.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.RestBean;
import com.example.entity.vo.request.user.AddressSaveReq;
import com.example.entity.vo.response.user.ShippingAddressResp;
import com.example.service.user.ShippingAddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-address")
public class ShippingAddressController {

    @Resource
    private ShippingAddressService shippingAddressService;

    //增删改查

    @Operation(summary = "获取所有收货地址信息")
    @GetMapping
    public RestBean<List<ShippingAddressResp>> getAllShippingAddress() {

        return RestBean.success(
                shippingAddressService.getAllShippingAddressByUserId(
                        StpUtil.getLoginIdAsLong()
                )
        );
    }
    @Operation(summary = "创建新的收货地址")
    @PostMapping
    public RestBean<Void> createShippingAddress(@Valid @RequestBody AddressSaveReq req){
        shippingAddressService.createAddress(req);
        return RestBean.success();
    }
    @Operation(summary = "删除收货地址")
    @DeleteMapping("/{id}")
    public RestBean<Void> deleteShippingAddress(@PathVariable long id){

        shippingAddressService.deleteAddress(id);
        return RestBean.success();
    }


    @Operation(summary = "通过ID更新收货地址")
    @PutMapping("/{id}")
    public RestBean<Void> updateShippingAddress(@PathVariable long id, @Valid @RequestBody AddressSaveReq req){

        shippingAddressService.updateAddress(id,req);
        return RestBean.success();
    }
}
