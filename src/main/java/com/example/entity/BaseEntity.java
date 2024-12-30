package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {

    Long creator;

    Long modifier;

    LocalDateTime createTime;

    LocalDateTime modifyTime;
}
