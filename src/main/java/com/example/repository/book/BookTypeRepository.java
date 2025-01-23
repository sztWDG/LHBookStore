package com.example.repository.book;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.BookType;
import com.example.mapper.book.BookTypeMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BookTypeRepository extends CrudRepository<BookTypeMapper, BookType> {

}
