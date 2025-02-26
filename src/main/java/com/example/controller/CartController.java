package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.cart.ShoppingAddReq;
import com.example.entity.vo.response.cart.ShoppingCartResp;
import com.example.service.cart.ShoppingCartService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping
    public RestBean<ShoppingCartResp> getCart(){
        return RestBean.success(shoppingCartService.getCart());
    }

    @PostMapping
    public RestBean<Void> addItem(@RequestBody @Valid ShoppingAddReq item) {

        shoppingCartService.addCart(item);
        return RestBean.success();
    }



    @PutMapping("/{id}")
    public RestBean<Void> updateCart(@PathVariable String id,Integer num) {
        shoppingCartService.updateCart(id,num);
        return RestBean.success();
    }



    @DeleteMapping("/{id}")
    public RestBean<Void> deleteItem(@PathVariable Integer id) {
        return RestBean.success();
    }

    /**
     * 是否可以直接删除整个购物车？
     * @param ids
     * @return
     */
    @PostMapping("/deleteAll")
    public RestBean<Void> deleteCart(@RequestBody @Valid List<Integer> ids) {

        return RestBean.success();
    }



    //应该通过ids进行购买？
    @PostMapping("/buy")
    public RestBean<Void> buyCombo(@RequestBody @Valid ShoppingAddReq item) {
        return RestBean.success();
    }

    @PostMapping("/buyAll")
    public RestBean<Void> buyAll(@RequestBody @Valid List<ShoppingAddReq> items) {
        return RestBean.success();
    }


}
