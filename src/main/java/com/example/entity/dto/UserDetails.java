package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.vo.response.user.UserDetailsResp;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@TableName("sys_user_details")
@AutoMapper(target = UserDetailsResp.class)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    @TableId(type = IdType.AUTO)
    long id;
    int gender;
    String phone;
    String qq;
    String wx;
    String description;
    long userId;

}
