package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ConfirmResetReq;
import com.example.entity.vo.request.EmailResetReq;
import com.example.entity.vo.request.UserLoginReq;
import com.example.entity.vo.request.UserRegisterReq;
import com.example.entity.vo.response.UserLoginResp;
import com.example.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    @Resource
    private AuthService authService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public RestBean<UserLoginResp> login(
            @RequestBody @Valid UserLoginReq req
    ) throws Exception {
        UserLoginResp resp = authService.login(req);
        return RestBean.success(resp);
    }

    //发送验证码
    @GetMapping("/code")
    public RestBean<Void> getCode(
            @RequestParam @Email String email,
            @RequestParam @Pattern(regexp = "(register|reset|modify)") String type,
            HttpServletRequest req
    ) throws Exception {
        authService.registerEmailVerify(type, email, req.getRemoteAddr());
        return RestBean.success();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public RestBean<Void> register(
            @RequestBody @Valid UserRegisterReq req
    ) throws Exception {
        authService.register(req);
        return RestBean.success();
    }

    //退出登录
    @GetMapping("/logout")
    public RestBean<Void> logout() {
        authService.logout();
        return RestBean.success();
    }

    //登录认证
    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetReq req) throws Exception {
        //Question!:这个地方返回值如何处理
        authService.resetConfirm(req);
        return RestBean.success();

    }

    @PostMapping("/reset-password")
    public RestBean<Void> resetConfirm(@RequestBody @Valid EmailResetReq req) throws Exception {
        authService.resetEmailAccountPassword(req);
        return RestBean.success();
    }

    private <T> RestBean<Void> messageHandle(T vo, Function<T,String> function){
        return messageHandle(() -> function.apply(vo));
    }

    private RestBean<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}
