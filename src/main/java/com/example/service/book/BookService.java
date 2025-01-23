package com.example.service.book;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.Book;
import com.example.entity.vo.request.book.BookSaveReq;
import com.example.entity.vo.response.book.BooksResp;
import com.example.exception.book.BookIsbnIsExistsException;
import com.example.exception.book.BookIsbnIsNotAllowModifyException;
import com.example.repository.book.BookRepository;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class BookService {
    @Resource
    private BookRepository bookRepository;
    @Resource
    private Converter converter;


    public IPage<BooksResp> showBooks(
            int pageIndex,
            int pageSize,
            String name,
            String author,
            Long typeId
    ) {
        Page<Book> page = Page.of(pageIndex, pageSize);
        //QxkTips:使用Lambda代替
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Book::getName, name)
                .like(Book::getAuthor, author);
        if (typeId != null) {
            queryWrapper.eq(Book::getTypeId, typeId);
        }
        //QxkTips1过滤掉已经下架的
//        IPage<BooksResp> temp = bookRepository.page(
//                page,
//                queryWrapper
//        ).convert(book -> converter.convert(book, BooksResp.class));
//        temp.setRecords(temp.getRecords().stream().filter(item ->
//                item.getStatus() != BookStatusEnum.De_List
//        ).toList());
        return bookRepository.page(
                page,
                queryWrapper
        ).convert(book -> converter.convert(book, BooksResp.class));
    }

    public void addBook(BookSaveReq req) {
        if (bookRepository.isBookIsbnExists(req.getIsbn())) {
            throw new BookIsbnIsExistsException();
        }
        Book book = converter.convert(req, Book.class);
        bookRepository.save(book);
    }

    public void updateBook(Long id, BookSaveReq req) {
        Book book = bookRepository.getById(id);
        if (!Objects.equals(book.getIsbn(), req.getIsbn())) {
            throw new BookIsbnIsNotAllowModifyException();
        }
        Book modifyBook = converter.convert(req, Book.class);
        bookRepository.updateById(modifyBook);
    }

    public void deleteBook(Long id) {
        bookRepository.removeById(id);
    }


}
