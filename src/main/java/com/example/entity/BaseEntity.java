package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {

    @Nullable
    @TableField(fill = FieldFill.INSERT)
    Long creator;

    @Nullable
    @TableField(fill = FieldFill.UPDATE)
    Long modifier;


    LocalDateTime createTime;
    LocalDateTime modifyTime;
}
