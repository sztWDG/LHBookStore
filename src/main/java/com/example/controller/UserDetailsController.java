package com.example.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.RestBean;
import com.example.entity.vo.request.DetailsSaveReq;
import com.example.entity.vo.response.UserDetailsResp;
import com.example.service.UserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {
    @Resource
    private UserDetailsService detailsService;

    @Operation(summary = "展示用户详细信息")
    @GetMapping("/details")
    public RestBean<UserDetailsResp> userDetails() {
        UserDetailsResp details = Optional.ofNullable(detailsService.getUserDetails(StpUtil.getLoginIdAsLong())).orElseGet(UserDetailsResp::new);
        return RestBean.success(details);
        //QxkQuestion: 可以这样做吗
        //return RestBean.success(detailsService.getUserDetails(StpUtil.getLoginIdAsLong()));
    }

    @Operation(summary = "保存用户详细信息")
    @PostMapping("/save-details")
    public RestBean<Void> saveUserDetails(@RequestBody @Valid DetailsSaveReq saveReq) {
        detailsService.saveUserDetails(StpUtil.getLoginIdAsLong(), saveReq);
        return RestBean.success();
    }
}
