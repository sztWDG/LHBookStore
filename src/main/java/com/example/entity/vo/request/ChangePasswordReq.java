package com.example.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ChangePasswordReq {
    @Length(min = 6, max = 20)
    String password;
    @Length(min = 6, max = 20)
    String newPassword;
}
