package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseEntity;
import com.example.entity.vo.response.auth.UserInfoResp;
import com.example.entity.vo.response.auth.UserLoginResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@AutoMappers({
        @AutoMapper(target = UserLoginResp.class),
        @AutoMapper(target = UserInfoResp.class),
})
@TableName("sys_user")
public class User extends BaseEntity {
    @TableId(type = IdType.AUTO)
    Long id;
    String username;
    String password;
    String email;
//    String role;
    @Nullable
    String avatar;
//    LocalDateTime registerTime;
}
