package com.example.entity.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginReq {
    @NotBlank(message = "不能为空")
    String text;
    @NotBlank(message = "不能为空")
    @Length(min = 6, max = 20)
    String password;
}
