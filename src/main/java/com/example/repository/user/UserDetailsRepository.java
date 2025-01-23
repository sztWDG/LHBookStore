package com.example.repository.user;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.UserDetails;
import com.example.mapper.user.UserDetailsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepository extends CrudRepository<UserDetailsMapper, UserDetails> {

    public UserDetails getUserDetailsByUserId(Long userId) {
        return this.query()
                .eq("user_id", userId)
                .select()
                .one();
//        return this.getById(userId); userId并非Details表的Id
    }



}
