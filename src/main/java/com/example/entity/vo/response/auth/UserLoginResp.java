package com.example.entity.vo.response.auth;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserLoginResp {
    long id;
    String username;
    String email;
    String avatar;
    String token;
    LocalDateTime expire;
    List<String> roles;
    List<String> permissions;
}
