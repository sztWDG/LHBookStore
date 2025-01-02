package com.example.service;

import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.dto.User;
import com.example.entity.vo.response.UserInfoResp;
import com.example.repository.UserRepository;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private Converter converter;

    public UserInfoResp getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userRepository.getById(userId);
        UserInfoResp userInfoResp = converter.convert(user, UserInfoResp.class);
        userInfoResp.setRole("user");
        return userInfoResp;
    }
}
