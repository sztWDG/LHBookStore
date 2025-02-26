package com.example.entity.vo.response.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDetailsResp {
    long id;
    int gender;
    String phone;
    String qq;
    String wx;
    String description;
    String username;

}
