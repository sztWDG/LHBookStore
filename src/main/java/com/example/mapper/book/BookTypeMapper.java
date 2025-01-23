package com.example.mapper.book;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.BookType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookTypeMapper extends BaseMapper<BookType> {
}
