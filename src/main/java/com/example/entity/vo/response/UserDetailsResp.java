package com.example.entity.vo.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDetailsResp {
    int id;
    int gender;
    String phone;
    String qq;
    String wx;
    String description;
    String username;

}
