package com.example.entity.vo.request.user;

import com.example.entity.dto.ShippingAddress;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AutoMapper(target = ShippingAddress.class)
public class AddressSaveReq {

    @NotBlank(message = "收件人名字不能为空")
    @Length(min = 3, max = 15)
    String name;
    @NotBlank
    @Length(min = 11, max = 11)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    String phone;
    @NotBlank(message = "收货地址不能为空")
    String address;

}
