package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.UserLoginReq;
import com.example.entity.vo.request.UserRegisterReq;
import com.example.entity.vo.response.UserLoginResp;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    @Resource
    private UserService userService;


    /**
     * 登录
     */
    @PostMapping("/login")
    public RestBean<UserLoginResp> login(
            @RequestBody @Valid UserLoginReq req
    ) throws Exception {
        UserLoginResp resp = userService.login(req);
        return RestBean.success(resp);
    }

    @GetMapping("/code")
    public RestBean<Void> getCode(
            @RequestParam @Email String email,
            @RequestParam @Pattern(regexp = "(register|reset|modify)") String type,
            HttpServletRequest req
    ) throws Exception {
        userService.registerEmailVerify(type, email, req.getRemoteAddr());
        return RestBean.success();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public RestBean<Void> register(
            @RequestBody @Valid UserRegisterReq req
    ) throws Exception {
        userService.register(req);
        return RestBean.success();
    }

    @GetMapping("/logout")
    public RestBean<Void> logout() {
        userService.logout();
        return RestBean.success();
    }

}
