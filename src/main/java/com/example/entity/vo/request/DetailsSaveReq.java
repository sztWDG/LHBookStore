package com.example.entity.vo.request;


import com.example.entity.dto.UserDetails;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AutoMapper(target = UserDetails.class)
public class DetailsSaveReq {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;
    @Min(0)
    @Max(1)
    int gender;
    @Pattern(regexp = "^1[3-9]\\d{9}$")
    @Length(max = 11)
    String phone;
    @Length(max = 13)
    String qq;
    @Length(max = 20)
    String wx;
    @Length(max = 300)
    String description;

}
