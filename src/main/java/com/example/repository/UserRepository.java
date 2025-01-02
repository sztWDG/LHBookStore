package com.example.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.User;
import com.example.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends CrudRepository<UserMapper, User> {

    public User findByUsernameOrEmail(String text) {
        return this.query()
                .eq("username", text)
                .or()
                .eq("email", text)
                .one();
    }

    public boolean usernameExists(String username) {
        return isUserExists(username);
    }

    public boolean emailExists(String email) {
        return isUserExists(email);
    }

    private boolean isUserExists(String text) {
        return this.query()
                .eq("username", text)
                .or()
                .eq("email", text)
                .exists();
    }


}
