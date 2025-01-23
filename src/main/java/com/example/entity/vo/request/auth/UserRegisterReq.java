package com.example.entity.vo.request.auth;

import com.example.entity.dto.User;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AutoMapper(target = User.class)
public class UserRegisterReq {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;
    @NotBlank(message = "不能为空")
    @Length(min = 6, max = 20)
    String password;
    @NotBlank(message = "不能为空")
    @Email
    String email;
    @NotBlank(message = "不能为空")
    String code;
}
