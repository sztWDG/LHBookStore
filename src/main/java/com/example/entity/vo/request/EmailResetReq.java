package com.example.entity.vo.request;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AutoMapper(target = ConfirmResetReq.class)
public class EmailResetReq {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
    @Length(min = 6, max = 20)
    String password;
}
