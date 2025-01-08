package com.example.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.BookType;
import com.example.mapper.BookTypeMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BookTypeRepository extends CrudRepository<BookTypeMapper, BookType> {

}
