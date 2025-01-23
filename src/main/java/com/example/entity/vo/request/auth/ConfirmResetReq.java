package com.example.entity.vo.request.auth;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ConfirmResetReq {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
}
