package com.example.mapper.book;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
