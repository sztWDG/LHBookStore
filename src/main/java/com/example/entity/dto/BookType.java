package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseEntity;
import com.example.entity.vo.response.book.BookTypeResp;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("book_type")
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = BookTypeResp.class)
public class BookType extends BaseEntity {
    @TableId(type = IdType.AUTO)
    Long id;
    String name;
}
