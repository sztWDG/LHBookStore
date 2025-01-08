package com.example.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.Book;
import com.example.mapper.BookMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository extends CrudRepository<BookMapper, Book> {
    public boolean isBookIsbnExists(String isbn) {
        return this.query().eq("isbn", isbn).exists();
    }
}
