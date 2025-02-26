package com.example.service.user;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.dto.User;
import com.example.entity.dto.UserDetails;
import com.example.entity.vo.request.user.DetailsSaveReq;
import com.example.entity.vo.response.user.UserDetailsResp;
import com.example.exception.auth.UsernameOrEmailExistsException;
import com.example.repository.user.UserDetailsRepository;
import com.example.repository.user.UserRepository;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsService {
    @Resource
    private UserDetailsRepository userDetailsRepository;
    @Resource
    private Converter converter;
    @Resource
    private UserRepository userRepository;

    public UserDetailsResp getUserDetails(long userId) {
        UserDetails userDetails = userDetailsRepository.getUserDetailsByUserId(userId);
        UserDetailsResp userDetailsResp = converter.convert(userDetails, UserDetailsResp.class);
        String username = userRepository.getById(userId).getUsername();
        userDetailsResp.setUsername(username);
        return userDetailsResp;
    }

    public synchronized void saveUserDetails(long userId, DetailsSaveReq saveReq) {
        User user = userRepository.getById(userId);
        //判断用户名是否存在
        if (!user.getUsername().equals(saveReq.getUsername())) {
            throw new UsernameOrEmailExistsException();
        }
        //更新username
        user.setUsername(saveReq.getUsername());
        userRepository.updateById(user);
        //转换并保存userDetails
        UserDetails details = converter.convert(saveReq, UserDetails.class);
//        UpdateWrapper<UserDetails> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("user_id", userId);
//        userDetailsRepository.getBaseMapper().update(details, updateWrapper);
        userDetailsRepository.update(
                details,
                new LambdaUpdateWrapper<UserDetails>().eq(UserDetails::getUserId, userId)
        );
    }

}
