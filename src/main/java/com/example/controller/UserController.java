package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.auth.UserInfoResp;
import com.example.service.user.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    // QxkQuestion3: 这边如何获得我的userId呢？
    @GetMapping("info")
    public RestBean<UserInfoResp> info(){
        UserInfoResp resp = userService.getUserInfo();
        return RestBean.success(resp);
    }



}
