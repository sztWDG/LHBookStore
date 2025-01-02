package com.example.entity;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {

    @Nullable
    Long creator;

    @Nullable
    Long modifier;

    LocalDateTime createTime;

    LocalDateTime modifyTime;
}
