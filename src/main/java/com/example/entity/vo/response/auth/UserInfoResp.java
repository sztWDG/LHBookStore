package com.example.entity.vo.response.auth;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserInfoResp {
    String username;
    String email;
    String role;
    Date registerTime;
}
