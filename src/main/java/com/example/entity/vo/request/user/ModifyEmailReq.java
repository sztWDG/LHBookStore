package com.example.entity.vo.request.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ModifyEmailReq {
    @Email
    String email;
    @Length(min = 6,max = 6)
    String code;
}
