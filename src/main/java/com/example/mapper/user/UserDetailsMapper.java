package com.example.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.UserDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDetailsMapper extends BaseMapper<UserDetails> {
}
