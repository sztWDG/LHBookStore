package com.example.repository.book;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.Book;
import com.example.mapper.book.BookMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository extends CrudRepository<BookMapper, Book> {
    public boolean isBookIsbnExists(String isbn) {
        return this.query().eq("isbn", isbn).exists();
    }

    public boolean getBooksByTypeId(Long typeId) {
//        return this.lambdaQuery().eq(Book::getTypeId, typeId).count() > 0;
        return this.lambdaQuery().eq(Book::getTypeId, typeId).list().isEmpty();
    }
}
